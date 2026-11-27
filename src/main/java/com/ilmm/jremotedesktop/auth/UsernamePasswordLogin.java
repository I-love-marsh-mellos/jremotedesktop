package com.ilmm.jremotedesktop.auth;

import com.ilmm.jremotedesktop.Capabilities;
import com.ilmm.jremotedesktop.LoginHttpExchange;

public class UsernamePasswordLogin implements AuthProvider {
    @Override
    public Capabilities login(LoginHttpExchange exchange) {
        return null;
    }

    @Override
    public Capabilities isValidSession(LoginHttpExchange exchange) {
        return null;
    }
}