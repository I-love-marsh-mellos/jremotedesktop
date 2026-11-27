package com.ilmm.jremotedesktop;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class UnauthorizedHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // TODO: add option to choose web directory location in config
        // hardcoded for now
        File file = new File("src/main/java/com/ilmm/jremotedesktop/web/login.html");
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                exchange.sendResponseHeaders(401, file.length());
                fis.transferTo(exchange.getResponseBody());
                exchange.close();
            }

            return;
        }

        exchange.sendResponseHeaders(401, -1);
    }
}