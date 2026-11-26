package com.ilmm.jremotedesktop;

import java.awt.event.KeyEvent;

public class JavaScriptToKeyEventMap {
    public static int[] getKeyCombination(String key) {
        key = key.toLowerCase();
        int code = getKeyCode(key);
        if (code > 0) return new int[]{code};

        if (code != -2) return null;

        switch (key) {
            case "^" -> {
                return new int[]{KeyEvent.VK_SHIFT, KeyEvent.VK_6};
            }

            case "#" -> {
                return new int[]{KeyEvent.VK_SHIFT, KeyEvent.VK_3};
            }

            case "%" -> {
                return new int[]{KeyEvent.VK_SHIFT, KeyEvent.VK_5};
            }
        }

        return null;
    }

    private static int getKeyCode(String key) {
        return switch (key) {
            // Alphabet keys
            case "a" -> KeyEvent.VK_A;
            case "b" -> KeyEvent.VK_B;
            case "c" -> KeyEvent.VK_C;
            case "d" -> KeyEvent.VK_D;
            case "e" -> KeyEvent.VK_E;
            case "f" -> KeyEvent.VK_F;
            case  "g"-> KeyEvent.VK_G;
            case "h" -> KeyEvent.VK_H;
            case "i" -> KeyEvent.VK_I;
            case "j" -> KeyEvent.VK_J;
            case "k" -> KeyEvent.VK_K;
            case "l" -> KeyEvent.VK_L;
            case "m" -> KeyEvent.VK_M;
            case "n" -> KeyEvent.VK_N;
            case "o" -> KeyEvent.VK_O;
            case "p" -> KeyEvent.VK_P;
            case "q" -> KeyEvent.VK_Q;
            case "r" -> KeyEvent.VK_R;
            case "s" -> KeyEvent.VK_S;
            case "t" -> KeyEvent.VK_T;
            case "u" -> KeyEvent.VK_U;
            case "v" -> KeyEvent.VK_V;
            case "w" -> KeyEvent.VK_W;
            case "x" -> KeyEvent.VK_X;
            case "y" -> KeyEvent.VK_Y;
            case "z" -> KeyEvent.VK_Z;

            // Number Keys
            case "1" -> KeyEvent.VK_1;
            case "2" -> KeyEvent.VK_2;
            case "3" -> KeyEvent.VK_3;
            case "4" -> KeyEvent.VK_4;
            case "5" -> KeyEvent.VK_5;
            case "6" -> KeyEvent.VK_6;
            case "7" -> KeyEvent.VK_7;
            case "8" -> KeyEvent.VK_8;
            case "9" -> KeyEvent.VK_9;
            case "0" -> KeyEvent.VK_0;

            // Number row
            case "!" -> KeyEvent.VK_EXCLAMATION_MARK;
            case "@" -> KeyEvent.VK_AT;
            case "#" -> -2;  // key combo stuff for the keys that aren't available in KeyEvent
            case "$" -> KeyEvent.VK_DOLLAR;
            case "%" -> -2;
            case "^" -> -2;
            case "&" -> KeyEvent.VK_AMPERSAND;
            case "*" -> KeyEvent.VK_ASTERISK;
            case "(" -> KeyEvent.VK_LEFT_PARENTHESIS;
            case ")" -> KeyEvent.VK_RIGHT_PARENTHESIS;
            case "_" -> KeyEvent.VK_UNDERSCORE;
            case "+" -> KeyEvent.VK_PLUS;

            // Function keys
            case "f1" -> KeyEvent.VK_F1;
            case "f2" -> KeyEvent.VK_F2;
            case "f3" -> KeyEvent.VK_F3;
            case "f4" -> KeyEvent.VK_F4;
            case "f5" -> KeyEvent.VK_F5;
            case "f6" -> KeyEvent.VK_F6;
            case "f7" -> KeyEvent.VK_F7;
            case "f8" -> KeyEvent.VK_F8;
            case "f9" -> KeyEvent.VK_F9;
            case "f10" -> KeyEvent.VK_F10;
            case "f11" -> KeyEvent.VK_F11;
            case "f12" -> KeyEvent.VK_F12;

            // Control keys
            case "enter" -> KeyEvent.VK_ENTER;
            case " " -> KeyEvent.VK_SPACE; // Space key
            case "control" -> KeyEvent.VK_CONTROL;
            case "alt" -> KeyEvent.VK_ALT;
            case "shift" -> KeyEvent.VK_SHIFT;
            case "tab" -> KeyEvent.VK_TAB;
            case "capslock" -> KeyEvent.VK_CAPS_LOCK;
            case "escape" -> KeyEvent.VK_ESCAPE;
            case "backspace" -> KeyEvent.VK_BACK_SPACE;
            case "delete" -> KeyEvent.VK_DELETE;
            case "insert" -> KeyEvent.VK_INSERT;
            case "home" -> KeyEvent.VK_HOME;
            case "end" -> KeyEvent.VK_END;
            case "pageup" -> KeyEvent.VK_PAGE_UP;
            case "pagedown" -> KeyEvent.VK_PAGE_DOWN;
            case "meta" -> KeyEvent.VK_WINDOWS;

            // Arrow keys
            case "arrowup" -> KeyEvent.VK_UP;
            case "arrowdown" -> KeyEvent.VK_DOWN;
            case "arrowleft" -> KeyEvent.VK_LEFT;
            case "arrowright" -> KeyEvent.VK_RIGHT;

            // Punctuation and symbols
            case "," -> KeyEvent.VK_COMMA;
            case "." -> KeyEvent.VK_PERIOD;
            case ";" -> KeyEvent.VK_SEMICOLON;
            case ":" -> KeyEvent.VK_COLON;
            case "=" -> KeyEvent.VK_EQUALS;
            case "-" -> KeyEvent.VK_MINUS;
            case "/" -> KeyEvent.VK_SLASH;
            case "\\" -> KeyEvent.VK_BACK_SLASH;
            case "'" -> KeyEvent.VK_QUOTE;
            case "[" -> KeyEvent.VK_OPEN_BRACKET;
            case "]" -> KeyEvent.VK_CLOSE_BRACKET;
            case "`" -> KeyEvent.VK_BACK_QUOTE;

            // Numpad keys
            case "numpad0" -> KeyEvent.VK_NUMPAD0;
            case "numpad1" -> KeyEvent.VK_NUMPAD1;
            case "numpad2" -> KeyEvent.VK_NUMPAD2;
            case "numpad3" -> KeyEvent.VK_NUMPAD3;
            case "numpad4" -> KeyEvent.VK_NUMPAD4;
            case "numpad5" -> KeyEvent.VK_NUMPAD5;
            case "numpad6" -> KeyEvent.VK_NUMPAD6;
            case "numpad7" -> KeyEvent.VK_NUMPAD7;
            case "numpad8" -> KeyEvent.VK_NUMPAD8;
            case "numpad9" -> KeyEvent.VK_NUMPAD9;
            case "numpadadd" -> KeyEvent.VK_ADD;
            case "numpadsubtract" -> KeyEvent.VK_SUBTRACT;
            case "numpadmultiply" -> KeyEvent.VK_MULTIPLY;
            case "numpaddivide" -> KeyEvent.VK_DIVIDE;
            case "numpaddecimal" -> KeyEvent.VK_DECIMAL;
            case "numpadenter" -> KeyEvent.VK_ENTER;

            // Miscellaneous
            case "numlock" -> KeyEvent.VK_NUM_LOCK;
            case "scrolllock" -> KeyEvent.VK_SCROLL_LOCK;
            case "pause" -> KeyEvent.VK_PAUSE;
            case "printscreen" -> KeyEvent.VK_PRINTSCREEN;
            case "contextmenu" -> KeyEvent.VK_CONTEXT_MENU;

            default -> -1;
        };
    }
}