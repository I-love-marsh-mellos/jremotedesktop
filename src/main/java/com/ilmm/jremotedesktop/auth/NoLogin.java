package com.ilmm.jremotedesktop.auth;

import com.ilmm.jremotedesktop.Capabilities;
import com.ilmm.jremotedesktop.LoginHttpExchange;

public class NoLogin implements AuthProvider {
    @Override
    public Capabilities login(LoginHttpExchange exchange) {
        return null; // TODO: config files for what different login providers can and can't do
    }

    @Override
    public Capabilities isValidSession(LoginHttpExchange exchange) {
        return null;
    }
}