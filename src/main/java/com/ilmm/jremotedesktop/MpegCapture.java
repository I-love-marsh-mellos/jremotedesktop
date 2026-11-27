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
    private final List<FrameCaptureListener> listeners = new ArrayList<>();
    private boolean autoStartStop = false;

    private static final byte[] BOUNDARY_AND_TYPE = "--frame\r\nContent-Type: image/".getBytes(StandardCharsets.US_ASCII);
    private static final byte[] CONTENT_LENGTH_PREFIX = "\r\nContent-Length: ".getBytes(StandardCharsets.US_ASCII);
    private static final byte[] HEADER_SUFFIX = "\r\n\r\n".getBytes(StandardCharsets.US_ASCII);
    private static final byte[] CRLF = "\r\n".getBytes(StandardCharsets.US_ASCII);
    private static final int LENGTH_DIGITS = 10;


    static class DirectByteArrayOutputStream extends ByteArrayOutputStream {
        public DirectByteArrayOutputStream(int size) { super(size); }
        // get bytes without copying to new byte array like in the built-in ByteArrayOutputStream
        public byte[] getBuffer() { return buf; }
    }

    public MpegCapture(Robot robot, Rectangle bounds, ImageFormat format) {
        this.robot = robot;
        this.bounds = bounds;
        this.format = format;
    }

    public void start() {
        captureThread = new Thread(() -> {
            // make sure image format cannot be changed while the capture loop is running
            String subtype = format.getType();
            byte[] typeBytes = subtype.getBytes(StandardCharsets.US_ASCII);
            DirectByteArrayOutputStream baos = new DirectByteArrayOutputStream(512 * 1024);

            while (!Thread.currentThread().isInterrupted()) {
                BufferedImage screenCapture = robot.createScreenCapture(bounds);
                baos.reset();

                try {
                    baos.write(BOUNDARY_AND_TYPE);
                    baos.write(typeBytes);
                    baos.write(CONTENT_LENGTH_PREFIX);
                    int lengthOffset = baos.size();
                    for (int i = 0; i < LENGTH_DIGITS; i++) {
                        baos.write(' ');
                    }
                    baos.write(HEADER_SUFFIX);

                    int imageStart = baos.size();
                    ImageIO.write(screenCapture, format.name(), baos);
                    int imageSize = baos.size() - imageStart;

                    byte[] buf = baos.getBuffer();
                    String lenStr = String.valueOf(imageSize);
                    int pad = LENGTH_DIGITS - lenStr.length();
                    for (int i = 0; i < lenStr.length(); i++) {
                        buf[lengthOffset + pad + i] = (byte) lenStr.charAt(i);
                    }

                    baos.write(CRLF);
                    sendAll(buf, baos.size());
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

    private void sendAll(byte[] framePacket, int totalLength) {
        for (FrameCaptureListener listener : listeners) {
            listener.framePacket(framePacket, totalLength);
        }
    }
}