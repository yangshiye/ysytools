package com.ysy.basetools.gqse.convert.impl;

import com.ysy.basetools.gqse.exceptions.GqSeEx;

import java.math.BigInteger;

/**
 * Created by Administrator on 2018/12/7.
 */
public class ByteConvert extends NumConvert<Byte> {

    @Override
    protected Byte convertByStr(String obj) {
        return Byte.parseByte(obj);
    }

    @Override
    protected Byte convertByNum(Number num) {
        if (num instanceof Byte) {
            return (Byte) num;
        } else if (num instanceof Short || num instanceof Integer || num instanceof Long) {
            long value = num.longValue();
            if (value > Byte.MAX_VALUE || value < Byte.MIN_VALUE) {
                throw new GqSeEx("value is out of range,value=" + num);
            } else {
                return (byte) value;
            }
        } else if (num instanceof BigInteger) {
            BigInteger bi = (BigInteger) num;
            return bi.byteValueExact();
        }
        throw new GqSeEx("can not convert num to byte, num=" + num + ",class=" + num.getClass());
    }

}
