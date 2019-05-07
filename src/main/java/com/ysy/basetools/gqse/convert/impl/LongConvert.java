package com.ysy.basetools.gqse.convert.impl;

import com.ysy.basetools.gqse.exceptions.GqSeEx;

import java.math.BigInteger;

/**
 * Created by Administrator on 2018/12/7.
 */
public class LongConvert extends NumConvert<Long> {

    @Override
    protected Long convertByStr(String obj) {
        return Long.parseLong(obj);
    }

    @Override
    protected Long convertByNum(Number num) {
        if (num instanceof Long) {
            return (Long) num;
        } else if (num instanceof Byte || num instanceof Short || num instanceof Integer) {
            return num.longValue();
        } else if (num instanceof BigInteger) {
            BigInteger bi = (BigInteger) num;
            return bi.longValue();
        }
        throw new GqSeEx("can not convert num to long, num=" + num + ",class=" + num.getClass());
    }
}
