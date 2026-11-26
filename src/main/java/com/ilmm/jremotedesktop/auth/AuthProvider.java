package com.ilmm.jremotedesktop.auth;

import com.ilmm.jremotedesktop.Capabilities;

import java.util.Map;

public interface AuthProvider {
    Capabilities login(Map<String, String> params);
}