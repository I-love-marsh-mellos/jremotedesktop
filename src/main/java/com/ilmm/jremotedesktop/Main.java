package com.ilmm.jremotedesktop;

import com.ilmm.jremotedesktop.auth.AuthRegistry;
import com.ilmm.jremotedesktop.auth.NoLogin;
import com.ilmm.jremotedesktop.auth.UsernamePasswordLogin;
import com.sun.net.httpserver.HttpServer;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;

public class Main {
    public static final String APPLICATION_NAME = "jremotedesktop";
    public static final int FRAME_RATE = 30;
    public static MpegCapture capture;
    public static Config config;

    public static void main(String[] args) {
        File file = new File(AppDataDir.getAppDataDirectory(APPLICATION_NAME), "config.txt");
        System.out.println("Config file " + file.getAbsolutePath());
        try {
            config = new Config(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (GraphicsEnvironment.isHeadless()) {
            diagnoseHeadlessEnvironment();
            return;
        }

        try {
            Robot robot = new Robot();

            if (isWaylandSession()) {
                System.err.println("Warning: Wayland session detected. Screen capture and input " +
                        "may be blocked by your desktop compositor.");
            }

            capture = new MpegCapture(robot, new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()), ImageFormat.JPG);
            capture.setAutoStartStop(true);
        } catch (HeadlessException e) {
            diagnoseHeadlessEnvironment();
            return;
        } catch (AWTException e) {
            System.err.println("Display server found, but AWT initialization failed: " + e.getMessage());
            return;
        } catch (AWTError | InternalError e) {
            System.err.println("Failed to connect to the X11 display server: " + e.getMessage());
            return;
        }

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(9000), 0);
            server.start();

            AuthRegistry registry = new AuthRegistry();
            registry.register(new UsernamePasswordLogin());
            registry.register(new NoLogin());

            AuthHandler auth = AuthHandler.newAuthHandler(registry, new VideoHandler(), new UnauthorizedHandler());
            server.createContext("/api/video", auth);
            server.createContext("/", AuthHandler.fromExisting(auth, exchange -> {
                File file1 = new File("src/main/java/com/ilmm/jremotedesktop/web/index.html");
                if (file1.exists()) {
                    exchange.sendResponseHeaders(200, file1.length());
                    try (FileInputStream fis = new FileInputStream(file1)) {
                        fis.transferTo(exchange.getResponseBody());
                    }
                    exchange.close();
                }
            }));
        } catch (BindException e) {
            System.out.println("Server port is already in use");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
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