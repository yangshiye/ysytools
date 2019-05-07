package com.ysy.basetools.model;

import java.io.Serializable;

/**
 * Created by guoqiang on 2017/12/6.
 */
public class FileDTO implements Serializable {
    private String name;
    private byte[] bytes;

    public FileDTO() {
    }

    public FileDTO(String name, byte[] bytes) {

        this.name = name;
        this.bytes = bytes;
    }

    public int getSize() {
        if (bytes == null) {
            return 0;
        }
        return bytes.length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
