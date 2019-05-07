package com.ysy.basetools.randomgen;

/**
 * Created by guoqiang on 2016/7/25.
 */
public class RandomGenUtil {
    public static final RandomGener RANDOM_GENER = new RandomGenerImpl();

    public static <T> T gen(Class<T> tClass) {
        return gen(tClass, null);
    }

    public static <T> T gen(Class<T> tClass, RandomGener gener) {
        if (gener == null) {
            gener = RANDOM_GENER;
        }
        T t = (T) gener.getValue(tClass);
        return t;
    }
}
