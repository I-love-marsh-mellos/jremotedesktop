package com.ilmm.jremotedesktop;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        if (GraphicsEnvironment.isHeadless()) {
            diagnoseHeadlessEnvironment();
            return;
        }

        try {
            Robot robot = new Robot();

            if (isWaylandSession()) {
                System.err.println("Warning: Wayland session detected. Screen capture and input simulation " +
                        "may be blocked or restricted by your desktop compositor.");
            }

//            MpegCapture capture = new MpegCapture(robot, );
        } catch (HeadlessException e) {
            diagnoseHeadlessEnvironment();
        } catch (AWTException e) {
            System.err.println("Display server found, but Robot initialization failed: " + e.getMessage());
        } catch (AWTError | InternalError e) {
            System.err.println("Failed to connect to the X11 display server: " + e.getMessage());
        }
    }

    private static boolean isWaylandSession() {
        String waylandDisplay = System.getenv("WAYLAND_DISPLAY");
        String sessionType = System.getenv("XDG_SESSION_TYPE");
        return (waylandDisplay != null && !waylandDisplay.isBlank())
                || "wayland".equalsIgnoreCase(sessionType);
    }

    private static void diagnoseHeadlessEnvironment() {
        System.err.println("Could not connect to a graphical display server.");

        String x11Display = System.getenv("DISPLAY");
        boolean hasX11 = x11Display != null && !x11Display.isBlank();

        if (Boolean.getBoolean("java.awt.headless")) {
            System.err.println("JVM was explicitly started in headless mode (-Djava.awt.headless=true).");
        } else if (isWaylandSession() && !hasX11) {
            System.err.println("Wayland session detected, but DISPLAY is unset. This program requires XWayland.");
        } else if (!isWaylandSession() && !hasX11) {
            System.err.println("No graphical session detected (both DISPLAY and WAYLAND_DISPLAY are unset).");
        }
    }
}