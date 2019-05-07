package com.ysy.basetools.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Created by guoqiang on 2017/10/20.
 */
public class UnsafeUtil {
    public static final Unsafe unsafe;

    static {
        try {
            unsafe = (Unsafe) ReflectUtil.getStaticFieldValue(Unsafe.class, "theUnsafe");
        } catch (Exception e) {
            throw new Error("get unsafe is error!");
        }
    }

    public static long getOffset(Field field) {
        return unsafe.objectFieldOffset(field);
    }

    public static void setValue(Object obj, long offset, Object value) {
        unsafe.putObject(obj, offset, value);
    }

    public static void setIntValue(Object obj, long offset, int value) {
        unsafe.putInt(obj, offset, value);
    }

    public static void setShortValue(Object obj, long offset, short value) {
        unsafe.putInt(obj, offset, value);
    }

    public static void setLongValue(Object obj, long offset, long value) {
        unsafe.putLong(obj, offset, value);
    }

    public static void setFloatValue(Object obj, long offset, float value) {
        unsafe.putFloat(obj, offset, value);
    }

    public static void setDoubleValue(Object obj, long offset, double value) {
        unsafe.putDouble(obj, offset, value);
    }

    public static void setByteValue(Object obj, long offset, byte value) {
        unsafe.putByte(obj, offset, value);
    }

    public static void setBoolValue(Object obj, long offset, boolean value) {
        unsafe.putBoolean(obj, offset, value);
    }

    public static void setCharValue(Object obj, long offset, char value) {
        unsafe.putChar(obj, offset, value);
    }

    public static Object getValue(Object obj, long offset) {
        return unsafe.getObject(obj, offset);
    }

    public static boolean getBooleanValue(Object obj, long offset) {
        return unsafe.getBoolean(obj, offset);
    }

    public static byte getByteValue(Object obj, long offset) {
        return unsafe.getByte(obj, offset);
    }

    public static char getCharValue(Object obj, long offset) {
        return unsafe.getChar(obj, offset);
    }

    public static double getDoubleValue(Object obj, long offset) {
        return unsafe.getDouble(obj, offset);
    }

    public static float getFloatValue(Object obj, long offset) {
        return unsafe.getFloat(obj, offset);
    }

    public static int getIntValue(Object obj, long offset) {
        return unsafe.getInt(obj, offset);
    }

    public static long getLongValue(Object obj, long offset) {
        return unsafe.getLong(obj, offset);
    }

    public static short getShortValue(Object obj, long offset) {
        return unsafe.getShort(obj, offset);
    }

    public static long arrayBaseOffset(Class clazz) {
        return unsafe.arrayBaseOffset(clazz);
    }

    public static int arrayIndexScale(Class clazz) {
        return unsafe.arrayIndexScale(clazz);
    }
}
