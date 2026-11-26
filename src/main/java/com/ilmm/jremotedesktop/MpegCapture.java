package com.ilmm.jremotedesktop;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MpegCapture {
    private final Robot robot;
    private Rectangle bounds;
    private ImageFormat format;
    private Thread captureThread;
    private List<FrameCaptureListener> listeners = new ArrayList<>();

    private static final byte[] CRLF = "\r\n".getBytes();
    private boolean autoStartStop = false;

    static class DirectByteArrayOutputStream extends ByteArrayOutputStream {
        public DirectByteArrayOutputStream(int size) { super(size); }
        public byte[] getBuffer() { return buf; }
    }

    public MpegCapture(Robot robot, Rectangle bounds, ImageFormat format) {
        this.robot = robot;
        this.bounds = bounds;
        this.format = format;
    }

    public void start() {
        captureThread = new Thread(() -> {
            DirectByteArrayOutputStream baos = new DirectByteArrayOutputStream(2 * 1024 * 1024);

            String headerTemplate = "--frame\r\nContent-Type: image/png\r\nContent-Length: %010d\r\n\r\n";
            int headerLen = String.format(headerTemplate, 0).getBytes(StandardCharsets.US_ASCII).length;

            while (!Thread.currentThread().isInterrupted()) {
                BufferedImage screenCapture = robot.createScreenCapture(bounds);
                baos.reset();

                try {
                    baos.write(new byte[headerLen]);

                    ImageIO.write(screenCapture, format.name(), baos);
                    int imageSize = baos.size() - headerLen;

                    baos.write(CRLF);
                    int totalLength = baos.size();

                    byte[] headerBytes = String.format(headerTemplate, imageSize).getBytes(StandardCharsets.US_ASCII);
                    System.arraycopy(headerBytes, 0, baos.getBuffer(), 0, headerLen);

                    sendAll(baos.getBuffer(), 0, totalLength);
                } catch (IOException e) {
                    break;
                }

                try {
                    Thread.sleep(1000 / Main.FRAME_RATE);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        captureThread.start();
    }

    public void stop() {
        captureThread.interrupt();
    }

    public void addListener(FrameCaptureListener listener) {
        if (autoStartStop && listeners.isEmpty()) start();
        listeners.add(listener);
    }

    public void removeListener(FrameCaptureListener listener) {
        listeners.remove(listener);
        if (autoStartStop && listeners.isEmpty()) stop();
    }

    public void setAutoStartStop(boolean autoStartStop) {
        this.autoStartStop = autoStartStop;
    }

    private void sendAll(byte[] framePacket, int i, int totalLength) {
        for (FrameCaptureListener listener : listeners) {
            listener.framePacket(framePacket, i, totalLength);
        }
    }
}