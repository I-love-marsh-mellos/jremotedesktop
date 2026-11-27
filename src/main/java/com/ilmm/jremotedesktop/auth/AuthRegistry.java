package com.ilmm.jremotedesktop.auth;

import java.util.ArrayList;
import java.util.List;

public class AuthRegistry {
    private List<AuthProvider> providers = new ArrayList<>();

    public AuthRegistry() {

    }

    public void register(AuthProvider provider) {
        providers.add(provider);
    }

    public List<AuthProvider> getProviders() {
        return providers;
    }
}