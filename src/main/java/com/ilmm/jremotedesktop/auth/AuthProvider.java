package com.ilmm.jremotedesktop.auth;

import com.ilmm.jremotedesktop.Capabilities;
import com.ilmm.jremotedesktop.LoginHttpExchange;

import java.util.Map;

public interface AuthProvider {
    Capabilities login(LoginHttpExchange exchange);
    Capabilities isValidSession(LoginHttpExchange exchange);
}