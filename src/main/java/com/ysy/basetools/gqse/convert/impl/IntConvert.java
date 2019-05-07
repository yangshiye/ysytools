package com.ysy.basetools.gqse.convert.impl;

import com.ysy.basetools.gqse.exceptions.GqSeEx;

import java.math.BigInteger;

/**
 * Created by Administrator on 2018/12/7.
 */
public class IntConvert extends NumConvert<Integer> {

    @Override
    protected Integer convertByStr(String obj) {
        return Integer.parseInt(obj);
    }

    @Override
    protected Integer convertByNum(Number num) {
        if (num instanceof Integer) {
            return (Integer) num;
        } else if (num instanceof Byte || num instanceof Short) {
            return num.intValue();
        } else if (num instanceof Long) {
            long value = (long) num;
            if (value > Integer.MAX_VALUE || value < Integer.MIN_VALUE) {
                throw new GqSeEx("value is out of range,value=" + num);
            } else {
                return (int) value;
            }
        } else if (num instanceof BigInteger) {
            BigInteger bi = (BigInteger) num;
            return bi.intValueExact();
        }
        throw new GqSeEx("can not convert num to int, num=" + num + ",class=" + num.getClass());
    }
}
