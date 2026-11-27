package com.ilmm.jremotedesktop;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class VideoHandler implements HttpHandlerWithHelpers {
    @Override
    public void handle(HttpExchange exchange, Capabilities capabilities) throws IOException {
        if (!capabilities.hasViewDisplay()) {
            exchange.sendResponseHeaders(403, -1);
            exchange.close();
            return;
        }

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
        Main.capture.addListener(new FrameCaptureListener() {
            @Override
            public void framePacket(byte[] jpegFrame) {
                try {
                    outputStream.write(jpegFrame);
                } catch (IOException e) {
                    exchange.close();
                    Main.capture.removeListener(this);
                }
            }
        });
    }
}