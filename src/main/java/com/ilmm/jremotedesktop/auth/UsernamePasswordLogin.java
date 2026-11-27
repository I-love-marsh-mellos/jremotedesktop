package com.ilmm.jremotedesktop.auth;

import com.ilmm.jremotedesktop.*;

import java.io.File;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;

public class UsernamePasswordLogin implements AuthProvider {
    private static final String SESSION_COOKIE_NAME = "_s";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private AutoSaveMap sessionMap = new AutoSaveMap(
            new File(AppDataDir.getAppDataDirectory(Main.APPLICATION_NAME), "login-sessions.txt")
    );

    @Override
    public Capabilities login(LoginHttpExchange exchange) {
        try {
            if (!"POST".equals(exchange.getRequestMethod())) return null;
            Map<String, String> params = Utils.parseQueryParams(new String(exchange.getRequestBody().readAllBytes()));
            String user = params.get("user");
            String pass = params.get("pass");

            if (Utils.constantTimeEquals(user, Main.config.getProperties().getProperty("login-username"))) {
                if (Utils.verifyPassword(pass, Utils.hashPassword(Main.config.getProperties().getProperty("login-password")))) {
                    System.out.println("correct login");
                    int setting = Main.config.getAsInt("login-capabilities",
                            Capabilities.KEYBOARD_INPUT | Capabilities.MOUSE_INPUT | Capabilities.VIEW_DISPLAY);

                    String sessionId = generateSessionToken();

                    sessionMap.put(sessionId, String.valueOf(setting));

                    exchange.getResponseHeaders().add("Set-Cookie",
                            SESSION_COOKIE_NAME + "=" + sessionId + "; HttpOnly; SameSite=Strict; Path=/");

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

        if (sessionId != null && sessionMap.containsKey(sessionId)) {
            try {
                // Retrieve and parse the saved capabilities integer
                int savedCapabilities = Integer.parseInt(sessionMap.get(sessionId));
                return new Capabilities(savedCapabilities);
            } catch (NumberFormatException e) {
                // Clean up corrupted session entry
                sessionMap.remove(sessionId);
            }
        }
        return null;
    }

    private String generateSessionToken() {
        byte[] bytes = new byte[32]; // 256-bit token
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}