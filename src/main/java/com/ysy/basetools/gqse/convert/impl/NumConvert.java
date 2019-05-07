package com.ysy.basetools.gqse.convert.impl;


import com.ysy.basetools.gqse.convert.Converter;
import com.ysy.basetools.gqse.exceptions.GqSeEx;

/**
 * Created by Administrator on 2018/12/7.
 */
public abstract class NumConvert<T extends Number> implements Converter<T> {
    @Override
    public T convert(Object obj) throws GqSeEx {
        if (obj == null) {
            return null;
        } else if (obj instanceof Number) {
            return convertByNum((Number) obj);
        } else if (obj instanceof String) {
            return convertByStr((String) obj);
        }
        throw new GqSeEx("type[" + obj.getClass() + "] can not convert String");
    }

    protected abstract T convertByStr(String obj);

    protected abstract T convertByNum(Number obj);
}
