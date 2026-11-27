package com.ilmm.jremotedesktop;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

public class LoginHttpExchange extends HttpExchange {
    private final HttpExchange exchange;

    public LoginHttpExchange(HttpExchange exchange) {
        this.exchange = exchange;
    }

    @Override
    public Headers getRequestHeaders() {
        return exchange.getRequestHeaders();
    }

    @Override
    public Headers getResponseHeaders() {
        return exchange.getResponseHeaders();
    }

    @Override
    public URI getRequestURI() {
        return exchange.getRequestURI();
    }

    @Override
    public String getRequestMethod() {
        return exchange.getRequestMethod();
    }

    @Override
    public HttpContext getHttpContext() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public InputStream getRequestBody() {
        return null;
    }

    @Override
    public OutputStream getResponseBody() {
        return OutputStream.nullOutputStream();
    }

    @Override
    public void sendResponseHeaders(int i, long l) {

    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return exchange.getRemoteAddress();
    }

    @Override
    public int getResponseCode() {
        return 0;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return exchange.getLocalAddress();
    }

    @Override
    public String getProtocol() {
        return exchange.getProtocol();
    }

    @Override
    public Object getAttribute(String s) {
        return exchange.getAttribute(s);
    }

    @Override
    public void setAttribute(String s, Object o) {

    }

    @Override
    public void setStreams(InputStream inputStream, OutputStream outputStream) {

    }

    @Override
    public HttpPrincipal getPrincipal() {
        return exchange.getPrincipal();
    }
}