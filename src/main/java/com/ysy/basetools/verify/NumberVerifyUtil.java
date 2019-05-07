package com.ysy.basetools.verify;

/**
 * Created by guoqiang on 2016/8/8.
 */
public class NumberVerifyUtil {


    public static boolean check(int value1, int value2, VerifyUtil.ContrastNumType type) {
        switch (type) {
            case GE:
                return value1 >= value2;
            case GT:
                return value1 > value2;
            case LE:
                return value1 <= value2;
            case LT:
                return value1 < value2;
        }
        return false;
    }

    public static boolean check(double value1, double value2, VerifyUtil.ContrastNumType type) {
        switch (type) {
            case GE:
                return value1 >= value2;
            case GT:
                return value1 > value2;
            case LE:
                return value1 <= value2;
            case LT:
                return value1 < value2;
        }
        return false;
    }

    public static boolean check(long value1, long value2, VerifyUtil.ContrastNumType type) {
        switch (type) {
            case GE:
                return value1 >= value2;
            case GT:
                return value1 > value2;
            case LE:
                return value1 <= value2;
            case LT:
                return value1 < value2;
        }
        return false;
    }

    public static boolean check(float value1, float value2, VerifyUtil.ContrastNumType type) {
        switch (type) {
            case GE:
                return value1 >= value2;
            case GT:
                return value1 > value2;
            case LE:
                return value1 <= value2;
            case LT:
                return value1 < value2;
        }
        return false;
    }

    public static boolean check(short value1, short value2, VerifyUtil.ContrastNumType type) {
        switch (type) {
            case GE:
                return value1 >= value2;
            case GT:
                return value1 > value2;
            case LE:
                return value1 <= value2;
            case LT:
                return value1 < value2;
        }
        return false;
    }

    public static boolean check(byte value1, byte value2, VerifyUtil.ContrastNumType type) {
        switch (type) {
            case GE:
                return value1 >= value2;
            case GT:
                return value1 > value2;
            case LE:
                return value1 <= value2;
            case LT:
                return value1 < value2;
        }
        return false;
    }
}
