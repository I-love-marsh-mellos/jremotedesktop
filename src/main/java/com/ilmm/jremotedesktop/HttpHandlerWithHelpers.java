package com.ilmm.jremotedesktop;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public interface HttpHandlerWithHelpers extends HttpHandler {
    void handle(HttpExchange exchange, Capabilities capabilities) throws IOException;

    @Override
    default void handle(HttpExchange exchange) throws IOException {
        handle(exchange, null);
    }
}