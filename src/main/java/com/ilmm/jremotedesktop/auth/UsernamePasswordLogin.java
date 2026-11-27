
package com.ilmm.jremotedesktop.auth;

import com.ilmm.jremotedesktop.*;

import java.io.File;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;

public class UsernamePasswordLogin implements AuthProvider {
    private static final String SESSION_COOKIE_NAME = "_s";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    // Session expires after 2 hours of inactivity
    private static final long SESSION_TIMEOUT = 120 * 60 * 1000L;

    private final AutoSaveMap sessionMap = new AutoSaveMap(
            new File(AppDataDir.getAppDataDirectory(Main.APPLICATION_NAME), "login-sessions.txt"));

    @Override
    public Capabilities login(LoginHttpExchange exchange) {
        try {
            if (!"POST".equals(exchange.getRequestMethod())) return null;

            Map<String, String> params = Utils.parseQueryParams(new String(exchange.getRequestBody().readAllBytes()));

            String user = params.get("user");
            String pass = params.get("pass");

            if (Utils.constantTimeEquals(user,
                    Main.config.getProperties().getProperty("login-username"))) {

                if (Utils.verifyPassword(pass, Utils.hashPassword(Main.config.getProperties().getProperty("login-password")))) {
                    int setting = Main.config.getAsInt("login-capabilities",
                            Capabilities.KEYBOARD_INPUT | Capabilities.MOUSE_INPUT | Capabilities.VIEW_DISPLAY);

                    String sessionId = generateSessionToken();

                    long expiration = System.currentTimeMillis() + SESSION_TIMEOUT;

                    sessionMap.put(sessionId, setting + ":" + expiration);

                    exchange.getResponseHeaders().add("Set-Cookie", SESSION_COOKIE_NAME + "=" + sessionId +
                            "; HttpOnly; SameSite=Strict; Path=/; Max-Age=" + (SESSION_TIMEOUT / 1000));

                    return new Capabilities(setting);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Capabilities isValidSession(LoginHttpExchange exchange) {
        String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");

        if (cookieHeader == null || cookieHeader.isEmpty()) {
            return null;
        }

        Map<String, String> cookies = Utils.parseCookieHeader(cookieHeader);
        String sessionId = cookies.get(SESSION_COOKIE_NAME);

        if (sessionId == null || !sessionMap.containsKey(sessionId)) {
            return null;
        }

        try {
            String sessionData = sessionMap.get(sessionId);
            String[] parts = sessionData.split(":");

            if (parts.length != 2) {
                sessionMap.remove(sessionId);
                return null;
            }

            int savedCapabilities = Integer.parseInt(parts[0]);
            long expiration = Long.parseLong(parts[1]);

            // Check whether the session has expired
            if (System.currentTimeMillis() >= expiration) {
                sessionMap.remove(sessionId);

                // Clear expired cookie in browser
                exchange.getResponseHeaders().add("Set-Cookie", SESSION_COOKIE_NAME + "=; HttpOnly; SameSite=Strict; Path=/; Max-Age=0");
                return null;
            }

            // Refresh expiration on activity
            long newExpiration = System.currentTimeMillis() + SESSION_TIMEOUT;

            sessionMap.put(sessionId, savedCapabilities + ":" + newExpiration);
            return new Capabilities(savedCapabilities);

        } catch (NumberFormatException e) {
            // Remove corrupted session entry
            sessionMap.remove(sessionId);
        }

        return null;
    }

    private String generateSessionToken() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);

        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}