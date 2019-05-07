package com.ysy.basetools.model;

/**
 * Created by guoqiang on 2017/6/14.
 */
public class ResUtil {
    public static String getMsg(Res res) {
        if (res == null) {
            return null;
        }
        return res.getMsg();
    }

    public static <T> T getT(Res<T> res) {
        if (res == null) {
            return null;
        }
        return res.getRe();
    }

    public static Throwable getE(Res res) {
        if (res == null) {
            return null;
        }
        return res.getE();
    }


    public static String getMsg(Result res) {
        if (res == null) {
            return null;
        }
        return res.getMsg();
    }

    public static <T> T getT(Result<T> res) {
        if (res == null) {
            return null;
        }
        return res.getData();
    }

    public static Throwable getE(Result res) {
        if (res == null) {
            return null;
        }
        return res.getE();
    }
}
