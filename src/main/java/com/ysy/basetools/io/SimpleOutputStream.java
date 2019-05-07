package com.ysy.basetools.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by guoqiang on 2017/8/28.
 */
public class SimpleOutputStream extends OutputStream {
    private byte[] buffer;
    private int size = 0;

    public byte[] getBytes() {
        byte[] temp = new byte[size];
        System.arraycopy(buffer, 0, temp, 0, size);
        return temp;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public SimpleOutputStream() {
        this(1024);
    }

    public SimpleOutputStream(int size) {
        buffer = new byte[size];
    }

    public byte getByte(int idx) {
        return buffer[idx];
    }

    @Override
    public void write(int b) throws IOException {
        if (size >= buffer.length) {
            byte[] temp = new byte[buffer.length * 2];
            System.arraycopy(buffer, 0, temp, 0, size);
            buffer = temp;
        }
        buffer[size++] = (byte) b;
    }

    public void write(byte b[], int off, int len) throws IOException {
        if (size + len > buffer.length) {
            byte[] temp = new byte[(size + len) * 2];
            System.arraycopy(buffer, 0, temp, 0, size);
            buffer = temp;
        }
        System.arraycopy(b, off, buffer, size, len);
        size += len;
    }

    public int getSize() {
        return size;
    }
}
