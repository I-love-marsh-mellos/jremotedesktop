package com.ilmm.jremotedesktop;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.HashMap;

public class AuthHandler implements HttpHandler {
    private final HttpHandler upstream;
    private final HashMap<String, String> sessions;
    private HttpHandler unauthorized;

    private AuthHandler(AuthHandler handler, HttpHandler upstream, HttpHandler unauthorized) {
        this.unauthorized = unauthorized;
        if (upstream == null) {
            throw new IllegalArgumentException("upstream cannot be null");
        }
        this.upstream = upstream;

        if (handler != null) {
            sessions = handler.sessions;
        } else {
            sessions = new HashMap<>();
        }
    }

    public static AuthHandler fromExisting(AuthHandler handler, HttpHandler upstream) {
        return new AuthHandler(handler, upstream, handler.unauthorized);
    }

    public static AuthHandler newAuthHandler(HttpHandler upstream, HttpHandler unauthorized) {
        return new AuthHandler(null, upstream, unauthorized);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (false) {
            upstream.handle(exchange);
        } else {
            unauthorized.handle(exchange);
        }
    }
}