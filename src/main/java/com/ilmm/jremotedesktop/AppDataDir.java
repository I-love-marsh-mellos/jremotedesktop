package com.ilmm.jremotedesktop;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AppDataDir {
    public static File getAppDataDirectory(String appName) {
        String os = System.getProperty("os.name").toLowerCase();
        String userHome = System.getProperty("user.home");

        if (os.contains("win")) {
            // Windows: C:\Users\<User>\AppData\Roaming
            String appData = System.getenv("APPDATA");
            if (appData != null && !appData.isEmpty()) {
                return Paths.get(appData, appName).toFile();
            }
            return Paths.get(userHome, "AppData", "Roaming", appName).toFile();
        } else {
            // Linux: Follows XDG Base Directory Specification (~/.local/share)
            String xdgDataHome = System.getenv("XDG_DATA_HOME");
            if (xdgDataHome != null && !xdgDataHome.isEmpty()) {
                return Paths.get(xdgDataHome, appName).toFile();
            }
            return Paths.get(userHome, ".local", "share", appName).toFile();
        }
    }
}