package com.ilmm.jremotedesktop.auth;

import com.ilmm.jremotedesktop.Capabilities;

import java.util.Map;

public class NoLogin implements AuthProvider {
    @Override
    public Capabilities login(Map<String, String> params) {
        return null; // TODO: config files for what different login providers can and can't do
    }
}