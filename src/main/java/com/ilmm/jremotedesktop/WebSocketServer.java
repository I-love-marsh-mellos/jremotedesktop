package com.ilmm.jremotedesktop;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class WebSocketServer {
    private final InetSocketAddress inet;
    private ServerSocket serverSocket;

    public WebSocketServer(InetSocketAddress inet) {
        this.inet = inet;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(inet.getPort(), 0, inet.getAddress());

        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    Socket socket = serverSocket.accept();
                    new Thread(() -> {
                        // non blocking connection loop

                    }).start();
                }
            } catch (IOException ignored) {
            }
        });

        thread.start();
    }
}