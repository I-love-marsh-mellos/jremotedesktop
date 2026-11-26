package com.ilmm.jremotedesktop;

import java.awt.event.KeyEvent;
import java.util.Map;

public class JavaScriptToKeyEventMap {
    public static int getKeyCode(String key) {
        return switch (key) {
            case "a" -> KeyEvent.VK_A;
            case "b" -> KeyEvent.VK_B;
            default -> -1;
        };
    }
}