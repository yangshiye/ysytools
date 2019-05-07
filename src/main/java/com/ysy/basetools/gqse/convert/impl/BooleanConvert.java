package com.ysy.basetools.gqse.convert.impl;

import com.ysy.basetools.gqse.convert.Converter;
import com.ysy.basetools.gqse.exceptions.GqSeEx;

/**
 * Created by Administrator on 2018/12/7.
 */
public class BooleanConvert implements Converter<Boolean> {
    @Override
    public Boolean convert(Object obj) throws GqSeEx {
        if (obj == null) {
            return null;
        } else if (obj instanceof Boolean) {
            return (Boolean) obj;
        } else if (obj instanceof String) {
            if ("TRUE".equalsIgnoreCase(obj.toString())) {
                return Boolean.TRUE;
            } else if ("FALSE".equalsIgnoreCase(obj.toString())) {
                return Boolean.FALSE;
            }
            throw new GqSeEx("val=" + obj + " can not convert Boolean");
        }
        throw new GqSeEx("type[" + obj.getClass() + "] can not convert Boolean");
    }
}
