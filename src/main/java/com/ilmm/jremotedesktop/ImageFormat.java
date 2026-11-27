package com.ilmm.jremotedesktop;

public enum ImageFormat {
    PNG("png"), JPG("jpeg");

    private final String type;

    private ImageFormat(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}