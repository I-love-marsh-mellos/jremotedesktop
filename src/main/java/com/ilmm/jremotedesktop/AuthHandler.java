package com.ilmm.jremotedesktop;

import com.ilmm.jremotedesktop.auth.AuthProvider;
import com.ilmm.jremotedesktop.auth.AuthRegistry;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.net.URI;
import java.util.HashMap;

public class AuthHandler implements HttpHandler {
    private final HttpHandler upstream;
    private final HashMap<String, String> sessions;
    private final HttpHandler unauthorized;
    private final AuthRegistry registry;

    private AuthHandler(AuthHandler handler, HttpHandler upstream, HttpHandler unauthorized, AuthRegistry registry) {
        this.unauthorized = unauthorized;
        this.registry = registry;
        this.upstream = upstream;

        if (upstream == null) {
            throw new IllegalArgumentException("upstream cannot be null");
        }

        if (registry == null) {
            throw new IllegalArgumentException("registry cannot be null");
        }

        if (handler != null) {
            sessions = handler.sessions;
        } else {
            sessions = new HashMap<>();
        }
    }

    public static AuthHandler fromExisting(AuthHandler handler, HttpHandler upstream) {
        return new AuthHandler(handler, upstream, handler.unauthorized, handler.registry);
    }

    public static AuthHandler newAuthHandler(AuthRegistry registry, HttpHandler upstream, HttpHandler unauthorized) {
        return new AuthHandler(null, upstream, unauthorized, registry);
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            URI uri = exchange.getRequestURI();
            String query = uri.getRawQuery();

            if (uri.getPath().equals("/api/login")) {
                if (exchange.getRequestMethod().equals("POST")) {
                    byte[] requestBodyBytes = exchange.getRequestBody().readAllBytes();
                    for (AuthProvider provider : registry.getProviders()) {
                        Capabilities login = provider.login(new LoginHttpExchange(exchange, requestBodyBytes));
//                if (login != null) isRecognized = true;
                    }
                } else {
                    for (AuthProvider provider : registry.getProviders()) {
                        Capabilities login = provider.login(new LoginHttpExchange(exchange, null));
//                if (login != null) isRecognized = true;
                    }
                }
            }

            int caps = 0;
            for (AuthProvider provider : registry.getProviders()) {
                Capabilities login = provider.isValidSession(new LoginHttpExchange(exchange, null));
                if (login != null && login.hasAnything()) {
                    if (login.hasViewDisplay()) caps |= Capabilities.VIEW_DISPLAY;
                    if (login.hasKeyboardInput()) caps |= Capabilities.KEYBOARD_INPUT;
                    if (login.hasMouseInput()) caps |= Capabilities.MOUSE_INPUT;
                }
            }

            Capabilities capabilities = new Capabilities(caps);

            // allow access to upstream if at least 1 AuthProvider recognized the client, some upstream handlers will include their own ACL checks
            if (capabilities.hasAnything()) {
                if (upstream instanceof HttpHandlerWithHelpers) {
                    HttpHandlerWithHelpers upstream1 = (HttpHandlerWithHelpers) upstream;
                    upstream1.handle(exchange, capabilities);
                } else {
                    upstream.handle(exchange);
                }
            } else {
                unauthorized.handle(exchange);
            }
        } catch (Throwable o) {
            o.printStackTrace();
        }
    }
}