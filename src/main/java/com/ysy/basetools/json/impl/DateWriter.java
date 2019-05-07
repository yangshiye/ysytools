package com.ysy.basetools.json.impl;

import com.ysy.basetools.json.JSONWriter;
import com.ysy.basetools.json.model.ReflectMap;
import com.ysy.basetools.util.TimeUtil;

import java.util.Date;

/**
 * Created by guoqiang on 2016/10/25.
 * OK
 */
public class DateWriter implements JSONWriter {
    @Override
    public Class getFormatClass() {
        return Date.class;
    }

    @Override
    public boolean isFormat(Object o) {
        return o instanceof Date;
    }

    @Override
    public StringBuilder getJSONStr(Object o, StringBuilder builder, String path, ReflectMap map) {
        if (o == null) {
            return builder.append("null");
        }
        return builder.append("\"").append(TimeUtil.formatAllTime((Date) o)).append("\"");
    }
}
