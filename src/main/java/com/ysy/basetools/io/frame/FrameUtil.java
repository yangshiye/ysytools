package com.ysy.basetools.io.frame;

import com.ysy.basetools.util.NumUtil;

import java.util.Arrays;

/**
 * Created by guoqiang on 2017/8/18.
 * 帧结构 FLAG(1字节) TYPE(2字节) LENGTH(4字节) DATA(LENGTH个字节) VERIFY(4字节) FLAG(1字节)
 */
public class FrameUtil {
    public static final int HEAD_LENGTH = 7;
    public static final int END_LENGTH = 5;
    public static final byte FLAG = 0x68;
    public static final short HEART_TYPE = 1;//心跳类型
    public static final short RS_GQSE_TYPE = 2;//KRYO格式化类型
    public static final short RS_STR_TYPE = 3;//字符串类型

    public static void main(String ag[]) {
        byte[] data = new byte[]{1, 2, 3, 4, 5};
        byte[] frame = createDataBytes(data, HEART_TYPE);
        System.out.println(Arrays.toString(frame));
        System.out.println(getType(frame));
        System.out.println(isHeartType(frame));
        System.out.println(isGQSEType(frame));
        System.out.println(isSTRType(frame));
        System.out.println(Arrays.toString(getDataBytes(frame)));
    }


    public static Short getType(byte[] bytes) {
        if (bytes == null || bytes.length < 3) {
            return null;
        } else {
            return toShort(bytes[1], bytes[2]);
        }
    }

    public static boolean isHeartType(byte[] frame) {
        return NumUtil.eq(HEART_TYPE, getType(frame));
    }

    public static boolean isGQSEType(byte[] frame) {
        return NumUtil.eq(RS_GQSE_TYPE, getType(frame));
    }

    public static boolean isSTRType(byte[] frame) {
        return NumUtil.eq(RS_STR_TYPE, getType(frame));
    }

    public static byte[] getDataBytes(byte[] frame) {
        if (frame.length <= 12) {
            return null;
        } else {
            byte[] data = new byte[frame.length - 12];
            System.arraycopy(frame, 7, data, 0, data.length);
            return data;
        }
    }

    public static byte[] createHeadBytes(byte[] bytes, short s) {
        byte[] result = new byte[HEAD_LENGTH];
        result[0] = FLAG;
        toBytes(s, result, 1);
        toBytes(bytes.length, result, 3);
        return result;
    }

    public static byte[] createDataBytes(byte[] bytes, short s) {
        byte[] result = new byte[HEAD_LENGTH + bytes.length + END_LENGTH];
        result[0] = FLAG;
        toBytes(s, result, 1);
        toBytes(bytes.length, result, 3);
        System.arraycopy(bytes, 0, result, HEAD_LENGTH, bytes.length);
        toBytes(calcVerify(bytes), result, HEAD_LENGTH + bytes.length);
        result[result.length - 1] = FLAG;
        return result;
    }

    public static byte[] createEndBytes(byte[] bytes) {
        byte[] result = new byte[END_LENGTH];
        result[4] = FLAG;
        toBytes(calcVerify(bytes), result, 0);
        return result;
    }

    public static int calcVerify(byte by[]) {
        return calcVerify(by, 0, by.length);
    }

    public static int calcVerify(byte by[], int off, int len) {
        byte b1 = 0;
        byte b2 = 0;
        byte b3 = 0;
        byte b4 = 0;
        byte k = 0;
        final int maxIdx = len + off;
        for (int i = off; i < maxIdx; i++, k++) {
            byte b = by[i];
            b1 = (byte) (b1 ^ b);
            b2 += b;

            byte s = (byte) (b + k);
            b3 = (byte) (b3 ^ s);
            b4 += s;
        }
        return toInt(b1, b2, b3, b4) + len;
    }

    public static int toInt(byte b4, byte b3, byte b2, byte b1) {
        int r = 0xff & b1;
        r = r << 8;
        r = r | (0xff & b2);
        r = r << 8;
        r = r | (0xff & b3);
        r = r << 8;
        r = r | (0xff & b4);
        return r;
    }

    public static byte[] toBytes(int v) {
        byte[] bytes = new byte[4];
        toBytes(v, bytes, 0);
        return bytes;
    }

    public static byte[] toBytes(short v) {
        byte[] bytes = new byte[2];
        toBytes(v, bytes, 0);
        return bytes;
    }

    public static void toBytes(int v, byte[] bytes, int off) {
        bytes[off] = (byte) (v & 0xff);
        bytes[1 + off] = (byte) (v >> 8 & 0xff);
        bytes[2 + off] = (byte) (v >> 16 & 0xff);
        bytes[3 + off] = (byte) (v >> 24 & 0xff);
    }

    public static byte[] toBytes(short v, byte[] bytes, int off) {
        bytes[off] = (byte) (v & 0xff);
        bytes[1 + off] = (byte) (v >> 8 & 0xff);
        return bytes;
    }

    public static short toShort(byte b2, byte b1) {
        short r = (short) (0xff & b1);
        r = (short) (r << 8);
        r = (short) (r | (0xff & b2));
        return r;
    }
}
