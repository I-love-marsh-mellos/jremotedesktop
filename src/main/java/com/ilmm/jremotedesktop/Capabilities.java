package com.ilmm.jremotedesktop;

import java.security.PublicKey;

public class Capabilities {
    public static final int VIEW_DISPLAY = 1;
    public static final int KEYBOARD_INPUT = 1 << 1;
    public static final int MOUSE_INPUT = 1 << 2;

    public Capabilities(int setting) {

    }
}