package com.ysy.basetools.extfield.impl;

import com.ysy.basetools.extfield.EF;
import com.ysy.basetools.extfield.ExtField;
import com.ysy.basetools.util.StrUtil;
import com.ysy.basetools.util.TimeUtil;

import java.util.Date;

/**
 * Created by Administrator on 2018/6/7.
 */
public class DateExtField implements ExtField<Date, Object> {

    @Override
    public String convert(Date fieldValue, Object obj, EF ef) {
        if (fieldValue != null) {
            String format = ef.extParam();
            if (StrUtil.empty(format)) {
                return TimeUtil.f4YMDHMS(fieldValue);
            }
            return TimeUtil.format(fieldValue, format);
        }
        return null;
    }
}
