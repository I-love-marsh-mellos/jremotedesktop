package com.ilmm.jremotedesktop;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Config {
    private final Properties properties;

    public final String defaultConfig = """
            # Http Server configuration
            port=9000
            host=127.0.0.1
            
            # Login methods and capabilities
            # Standard username and password (leave blank to disable)
            login-username=
            login-password=
            login-capabilities=
            
            # No login required (best for view only)
            
            """;
    private final File file;

    public Config(File file) throws IOException {
        this.file = file;
        properties = new Properties();
        properties.load(Files.newBufferedReader(file.toPath()));
    }

    public void save() throws IOException {
        properties.store(Files.newBufferedWriter(file.toPath()), "");
    }

    public Properties getProperties() {
        return properties;
    }

    public int getAsInt(String key, int defaultValue) {
        String property = properties.getProperty(key);
        if (property == null) return defaultValue;

        try {
            return Integer.parseInt(property);
        } catch (NumberFormatException e) {
            System.err.println("Config property read error "  + key);
            System.out.println(e.getMessage());
            return defaultValue;
        }
    }

    // TODO: add type and format verification
    public List<String> verifyConfig() {
        ArrayList<String> errors = new ArrayList<>();
        for (Object o : properties.keySet()) {
            String key = (String) o;
            String property = properties.getProperty(key);
        }

        return errors;
    }
}