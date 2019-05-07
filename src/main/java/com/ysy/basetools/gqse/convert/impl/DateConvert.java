package com.ysy.basetools.gqse.convert.impl;

import com.ysy.basetools.gqse.convert.Converter;
import com.ysy.basetools.gqse.exceptions.GqSeEx;
import com.ysy.basetools.util.TimeUtil;

import java.util.Date;

/**
 * Created by Administrator on 2018/12/7.
 */
public class DateConvert implements Converter<Date> {
    private static final String format = "yyyy-MM-dd HH:mm:ss.SSS";

    @Override
    public Date convert(Object obj) throws GqSeEx {
        if (obj == null) {
            return null;
        } else if (obj instanceof Date) {
            return (Date) obj;
        } else if (obj instanceof String) {
            if (((String) obj).length() != format.length()) {
                throw new GqSeEx("dateStr=" + obj + ",format type must be yyyy-MM-dd HH:mm:ss.SSS");
            }
            Date date = TimeUtil.parse((String) obj, format);
            if (date == null) {
                throw new GqSeEx("dateStr=" + obj + ", can not convert Date");
            }
            return date;
        }
        throw new GqSeEx("type[" + obj.getClass() + "] can not convert Date");
    }

}
