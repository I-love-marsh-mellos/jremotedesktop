package com.ilmm.jremotedesktop;

public class ScreenResizeWatcher {
    private Runnable runnable;

    public ScreenResizeWatcher() {

    }

    public void start() {

    }

    public void onScreenSizeChanged(Runnable runnable) {
        this.runnable = runnable;
    }
}