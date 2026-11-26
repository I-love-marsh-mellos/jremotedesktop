package com.ilmm.jremotedesktop;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class VideoHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            handleVideoStream(exchange);
        } catch (IOException e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(500, -1);
            exchange.close();
        }
    }

    private static void handleVideoStream(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "multipart/x-mixed-replace; boundary=--frame");
        exchange.sendResponseHeaders(200, 0);

        OutputStream outputStream = exchange.getResponseBody();
        Main.capture.addListener((jpegFrame, i, totalLength) -> {
            try {
                outputStream.write(jpegFrame);
            } catch (IOException e) {
                e.printStackTrace();
                exchange.close();
            }
        });
    }
}