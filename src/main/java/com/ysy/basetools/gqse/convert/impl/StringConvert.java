package com.ysy.basetools.gqse.convert.impl;

import com.ysy.basetools.gqse.convert.Converter;
import com.ysy.basetools.gqse.exceptions.GqSeEx;
import com.ysy.basetools.util.TimeUtil;

import java.util.Date;

/**
 * Created by Administrator on 2018/12/7.
 */
public class StringConvert implements Converter<String> {
    @Override
    public String convert(Object obj) throws RuntimeException {
        if (obj == null) {
            return null;
        } else if (obj instanceof String) {
            return (String) obj;
        } else if (obj instanceof CharSequence) {
            return obj.toString();
        } else if (obj instanceof Number) {
            return obj.toString();
        } else if (obj instanceof Character) {
            return obj.toString();
        } else if (obj instanceof Boolean) {
            return obj.toString();
        } else if (obj instanceof Date) {
            return TimeUtil.formatAllTime((Date) obj);
        }
        throw new GqSeEx("type[" + obj.getClass() + "] can not convert String");
    }
}
