package com.ysy.basetools.util;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Administrator on 2018/10/16.
 */
public class ZIPUtil {
    public static byte[] writeZIP(byte bytes[], String name, int offset, int len) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length);
        ZipOutputStream zos = new ZipOutputStream(baos);
        zos.putNextEntry(new ZipEntry(name));
        zos.write(bytes, offset, len);
        zos.flush();
        zos.close();
        byte[] result = baos.toByteArray();
        baos.close();
        return result;
    }

    public static byte[] writeZIP(byte bytes[], String name) throws IOException {
        return writeZIP(bytes, name, 0, bytes.length);
    }

    public static byte[] writeZIP(Map<String, byte[]> map) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(64 * 1024);
        ZipOutputStream zos = new ZipOutputStream(baos);

        for (String key : map.keySet()) {
            byte[] bytes = map.get(key);
            if (key == null || bytes == null) {
                throw new NullPointerException("key is null or,bytes==null,key=" + key);
            } else {
                zos.putNextEntry(new ZipEntry(key));
                zos.write(bytes);
            }
        }
        zos.flush();
        zos.close();
        byte[] result = baos.toByteArray();
        baos.close();
        return result;
    }
}
