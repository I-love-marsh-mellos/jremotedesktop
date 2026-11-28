package com.ilmm.jremotedesktop;

public interface WebSocketListener {
    void onOpen (WebSocketServer.WebSocketConnection webSocketConnection);
    void onMessage(WebSocketServer.WebSocketConnection webSocketConnection, String message);
    void onDisconnect(WebSocketServer.WebSocketConnection webSocketConnection);
}