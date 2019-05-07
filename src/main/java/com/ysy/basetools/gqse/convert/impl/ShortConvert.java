package com.ysy.basetools.gqse.convert.impl;

import com.ysy.basetools.gqse.exceptions.GqSeEx;

import java.math.BigInteger;

/**
 * Created by Administrator on 2018/12/7.
 */
public class ShortConvert extends NumConvert<Short> {

    @Override
    protected Short convertByStr(String obj) {
        return Short.parseShort(obj);
    }

    @Override
    protected Short convertByNum(Number num) {
        if (num instanceof Short) {
            return (Short) num;
        } else if (num instanceof Byte) {
            return num.shortValue();
        } else if (num instanceof Integer || num instanceof Long) {
            long value = num.longValue();
            if (value > Short.MAX_VALUE || value < Short.MIN_VALUE) {
                throw new GqSeEx("value is out of range,value=" + num);
            } else {
                return (short) value;
            }
        } else if (num instanceof BigInteger) {
            BigInteger bi = (BigInteger) num;
            return bi.shortValueExact();
        }
        throw new GqSeEx("can not convert num to short, num=" + num + ",class=" + num.getClass());
    }

}
