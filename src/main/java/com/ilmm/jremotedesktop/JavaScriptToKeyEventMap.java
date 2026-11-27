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

        switch (key) {
            // Alphabet keys
            case "a":
                return KeyEvent.VK_A;
            case "b":
                return KeyEvent.VK_B;
            case "c":
                return KeyEvent.VK_C;
            case "d":
                return KeyEvent.VK_D;
            case "e":
                return KeyEvent.VK_E;
            case "f":
                return KeyEvent.VK_F;
            case "g":
                return KeyEvent.VK_G;
            case "h":
                return KeyEvent.VK_H;
            case "i":
                return KeyEvent.VK_I;
            case "j":
                return KeyEvent.VK_J;
            case "k":
                return KeyEvent.VK_K;
            case "l":
                return KeyEvent.VK_L;
            case "m":
                return KeyEvent.VK_M;
            case "n":
                return KeyEvent.VK_N;
            case "o":
                return KeyEvent.VK_O;
            case "p":
                return KeyEvent.VK_P;
            case "q":
                return KeyEvent.VK_Q;
            case "r":
                return KeyEvent.VK_R;
            case "s":
                return KeyEvent.VK_S;
            case "t":
                return KeyEvent.VK_T;
            case "u":
                return KeyEvent.VK_U;
            case "v":
                return KeyEvent.VK_V;
            case "w":
                return KeyEvent.VK_W;
            case "x":
                return KeyEvent.VK_X;
            case "y":
                return KeyEvent.VK_Y;
            case "z":
                return KeyEvent.VK_Z;

            // Number Keys
            case "1":
                return KeyEvent.VK_1;
            case "2":
                return KeyEvent.VK_2;
            case "3":
                return KeyEvent.VK_3;
            case "4":
                return KeyEvent.VK_4;
            case "5":
                return KeyEvent.VK_5;
            case "6":
                return KeyEvent.VK_6;
            case "7":
                return KeyEvent.VK_7;
            case "8":
                return KeyEvent.VK_8;
            case "9":
                return KeyEvent.VK_9;
            case "0":
                return KeyEvent.VK_0;

            // Number row
            case "!":
                return KeyEvent.VK_EXCLAMATION_MARK;
            case "@":
                return KeyEvent.VK_AT;
            case "#":
                // key combo stuff for the keys that aren't available in KeyEvent
                return -2;
            case "$":
                return KeyEvent.VK_DOLLAR;
            case "%":
                return -2;
            case "^":
                return -2;
            case "&":
                return KeyEvent.VK_AMPERSAND;
            case "*":
                return KeyEvent.VK_ASTERISK;
            case "(":
                return KeyEvent.VK_LEFT_PARENTHESIS;
            case ")":
                return KeyEvent.VK_RIGHT_PARENTHESIS;
            case "_":
                return KeyEvent.VK_UNDERSCORE;
            case "+":
                return KeyEvent.VK_PLUS;

            // Function keys
            case "f1":
                return KeyEvent.VK_F1;
            case "f2":
                return KeyEvent.VK_F2;
            case "f3":
                return KeyEvent.VK_F3;
            case "f4":
                return KeyEvent.VK_F4;
            case "f5":
                return KeyEvent.VK_F5;
            case "f6":
                return KeyEvent.VK_F6;
            case "f7":
                return KeyEvent.VK_F7;
            case "f8":
                return KeyEvent.VK_F8;
            case "f9":
                return KeyEvent.VK_F9;
            case "f10":
                return KeyEvent.VK_F10;
            case "f11":
                return KeyEvent.VK_F11;
            case "f12":
                return KeyEvent.VK_F12;

            // Control keys
            case "enter":
                return KeyEvent.VK_ENTER;
            case " ":
                return KeyEvent.VK_SPACE;
            case "control":
                return KeyEvent.VK_CONTROL;
            case "alt":
                return KeyEvent.VK_ALT;
            case "shift":
                return KeyEvent.VK_SHIFT;
            case "tab":
                return KeyEvent.VK_TAB;
            case "capslock":
                return KeyEvent.VK_CAPS_LOCK;
            case "escape":
                return KeyEvent.VK_ESCAPE;
            case "backspace":
                return KeyEvent.VK_BACK_SPACE;
            case "delete":
                return KeyEvent.VK_DELETE;
            case "insert":
                return KeyEvent.VK_INSERT;
            case "home":
                return KeyEvent.VK_HOME;
            case "end":
                return KeyEvent.VK_END;
            case "pageup":
                return KeyEvent.VK_PAGE_UP;
            case "pagedown":
                return KeyEvent.VK_PAGE_DOWN;
            case "meta":
                return KeyEvent.VK_WINDOWS;

            // Arrow keys
            case "arrowup":
                return KeyEvent.VK_UP;
            case "arrowdown":
                return KeyEvent.VK_DOWN;
            case "arrowleft":
                return KeyEvent.VK_LEFT;
            case "arrowright":
                return KeyEvent.VK_RIGHT;

            // Punctuation and symbols
            case ",":
                return KeyEvent.VK_COMMA;
            case ".":
                return KeyEvent.VK_PERIOD;
            case ";":
                return KeyEvent.VK_SEMICOLON;
            case ":":
                return KeyEvent.VK_COLON;
            case "=":
                return KeyEvent.VK_EQUALS;
            case "-":
                return KeyEvent.VK_MINUS;
            case "/":
                return KeyEvent.VK_SLASH;
            case "\\":
                return KeyEvent.VK_BACK_SLASH;
            case "'":
                return KeyEvent.VK_QUOTE;
            case "[":
                return KeyEvent.VK_OPEN_BRACKET;
            case "]":
                return KeyEvent.VK_CLOSE_BRACKET;
            case "`":
                return KeyEvent.VK_BACK_QUOTE;

            // Numpad keys
            case "numpad0":
                return KeyEvent.VK_NUMPAD0;
            case "numpad1":
                return KeyEvent.VK_NUMPAD1;
            case "numpad2":
                return KeyEvent.VK_NUMPAD2;
            case "numpad3":
                return KeyEvent.VK_NUMPAD3;
            case "numpad4":
                return KeyEvent.VK_NUMPAD4;
            case "numpad5":
                return KeyEvent.VK_NUMPAD5;
            case "numpad6":
                return KeyEvent.VK_NUMPAD6;
            case "numpad7":
                return KeyEvent.VK_NUMPAD7;
            case "numpad8":
                return KeyEvent.VK_NUMPAD8;
            case "numpad9":
                return KeyEvent.VK_NUMPAD9;
            case "numpadadd":
                return KeyEvent.VK_ADD;
            case "numpadsubtract":
                return KeyEvent.VK_SUBTRACT;
            case "numpadmultiply":
                return KeyEvent.VK_MULTIPLY;
            case "numpaddivide":
                return KeyEvent.VK_DIVIDE;
            case "numpaddecimal":
                return KeyEvent.VK_DECIMAL;
            case "numpadenter":
                return KeyEvent.VK_ENTER;
            case "numlock":
                return KeyEvent.VK_NUM_LOCK;
            case "scrolllock":
                return KeyEvent.VK_SCROLL_LOCK;

            // Miscellaneous
            case "pause":
                return KeyEvent.VK_PAUSE;
            case "printscreen":
                return KeyEvent.VK_PRINTSCREEN;
            case "contextmenu":
                return KeyEvent.VK_CONTEXT_MENU;
            default:
                return -1;
        }
    }
}