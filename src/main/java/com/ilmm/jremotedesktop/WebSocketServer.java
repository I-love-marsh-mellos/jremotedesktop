package com.ilmm.jremotedesktop;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

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
                    new Thread(new WebSocketConnection(socket)).start();
                }
            } catch (IOException ignored) {
            }
        });

        thread.start();
    }


    public static class WebSocketConnection implements Runnable {
        private final InputStream in;
        private final OutputStream out;
        private final Socket socket;

        public WebSocketConnection(Socket socket) throws IOException {
            this.socket = socket;
            this.in = socket.getInputStream();
            this.out = socket.getOutputStream();
        }

        private String readMessage() throws IOException {
            int b = in.read();
            if (b == -1) return null;
            b = in.read();
            if (b == -1) return null;
            int payloadLength = b & 0x7F;
            if (payloadLength == 126) {
                payloadLength = (in.read() << 8) | in.read();
            } else if (payloadLength == 127) {
                payloadLength = 0;
            }

            byte[] mask = new byte[4];
            in.read(mask, 0, 4);
            byte[] payload = new byte[payloadLength];
            int read = 0;
            while (read < payloadLength) {
                int r = in.read(payload, read, payloadLength - read);
                if (r == -1) break;
                read += r;
            }
            for (int i = 0; i < payloadLength; i++) {
                payload[i] = (byte) (payload[i] ^ mask[i % 4]);
            }

            return new String(payload, StandardCharsets.UTF_8);
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                String key = null;
                while (!(line = reader.readLine()).isEmpty()) {
                    if (line.startsWith("Sec-WebSocket-Key:")) {
                        key = line.substring("Sec-WebSocket-Key:".length()).trim();
                    }
                }
                if (key == null) {
                    socket.close();
                    return;
                }
                String acceptKey = generateAcceptKey(key);
                String response = """                                                                                                                                                              
                        HTTP/1.1 101 Switching Protocols\r
                        Upgrade: websocket\r
                        Connection: Upgrade\r
                        Sec-WebSocket-Accept:""" + acceptKey + "\r\n\r\n";
                out.write(response.getBytes(StandardCharsets.UTF_8));
                out.flush();

                while (!socket.isClosed()) {
                    System.out.println("WS recv " + readMessage());
                }
            } catch (Exception ignored) {
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
        }

        private String generateAcceptKey(String key) throws Exception {
            String concat = key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest(concat.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        }
    }

    public static void main(String[] args) throws IOException {
        WebSocketServer server = new WebSocketServer(new InetSocketAddress(9001));
        server.start();
    }
}