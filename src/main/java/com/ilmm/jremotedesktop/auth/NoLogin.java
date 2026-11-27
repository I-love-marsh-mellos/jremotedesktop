package com.ilmm.jremotedesktop.auth;

import com.ilmm.jremotedesktop.Capabilities;
import com.ilmm.jremotedesktop.LoginHttpExchange;
import com.ilmm.jremotedesktop.Main;

public class NoLogin implements AuthProvider {
    @Override
    public Capabilities login(LoginHttpExchange exchange) {
        int setting = Main.config.getAsInt("no-login-capabilities", 0);
        return new Capabilities(setting);
    }

    @Override
    public Capabilities isValidSession(LoginHttpExchange exchange) {
        int setting = Main.config.getAsInt("no-login-capabilities", 0);
        return new Capabilities(setting);
    }
}