package com.ilmm.jremotedesktop;

public interface FrameCaptureListener {
    void framePacket(byte[] jpegFrame, int i, int totalLength);
}