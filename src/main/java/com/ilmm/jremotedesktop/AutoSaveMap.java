package com.ilmm.jremotedesktop;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class AutoSaveMap extends HashMap<String, String> {
    private final Path filePath;

    public AutoSaveMap(File file) {
        this.filePath = file.toPath();
        loadFromFile();
    }

    @Override
    public String put(String key, String value) {
        String previous = super.put(key, value);
        saveToFile();
        return previous;
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        super.putAll(m);
        saveToFile();
    }

    @Override
    public String remove(Object key) {
        String removed = super.remove(key);
        if (removed != null) {
            saveToFile();
        }
        return removed;
    }

    @Override
    public void clear() {
        super.clear();
        saveToFile();
    }

    private synchronized void saveToFile() {
        Properties props = new Properties();
        props.putAll(this);
        try (OutputStream out = Files.newOutputStream(filePath)) {
            props.store(out, "Auto-saved HashMap data");
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to autosave map to " + filePath, e);
        }
    }

    private void loadFromFile() {
        if (!Files.exists(filePath)) return;

        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(filePath)) {
            props.load(in);
            for (String key : props.stringPropertyNames()) {
                super.put(key, props.getProperty(key)); // Use super.put to avoid saving while loading
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load map from " + filePath, e);
        }
    }
}