package com.ilmm.jremotedesktop;

public record Capabilities(int setting) {
    public static final int VIEW_DISPLAY = 1;
    public static final int KEYBOARD_INPUT = 1 << 1;
    public static final int MOUSE_INPUT = 1 << 2;

    public boolean hasViewDisplay() {
        return (setting & VIEW_DISPLAY) != 0;
    }

    public boolean hasKeyboardInput() {
        return (setting & KEYBOARD_INPUT) != 0;
    }

    public boolean hasMouseInput() {
        return (setting & MOUSE_INPUT) != 0;
    }
}