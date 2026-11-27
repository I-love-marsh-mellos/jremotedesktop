package com.ilmm.jremotedesktop;

import com.ilmm.jremotedesktop.auth.AuthProvider;
import com.ilmm.jremotedesktop.auth.AuthRegistry;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class AuthHandler implements HttpHandler {
    private final HttpHandler upstream;
    private final HashMap<String, String> sessions;
    private HttpHandler unauthorized;
    private AuthRegistry registry;

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
    public void handle(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        String query = uri.getRawQuery();
        Map<String, String> params = Utils.parseQueryParams(query);

        boolean isRecognized = false;
        for (AuthProvider provider : registry.getProviders()) {
            Capabilities login = provider.login(params);
            if (login != null) isRecognized = true;
        }

        // allow access to upstream if at least 1 AuthProvider recognized the client, some upstream handlers will include their own ACL checks
        if (isRecognized) {
            upstream.handle(exchange);
        } else {
            unauthorized.handle(exchange);
        }
    }
}