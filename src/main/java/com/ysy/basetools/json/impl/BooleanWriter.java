package com.ysy.basetools.json.impl;

import com.ysy.basetools.json.JSONWriter;
import com.ysy.basetools.json.model.ReflectMap;

/**
 * Created by guoqiang on 2016/10/25.
 * OK
 */
public class BooleanWriter implements JSONWriter {
    @Override
    public Class getFormatClass() {
        return Boolean.class;
    }

    @Override
    public boolean isFormat(Object o) {
        return o instanceof Boolean;
    }

    @Override
    public StringBuilder getJSONStr(Object o, StringBuilder builder, String path, ReflectMap map) {
        return o == null ? builder.append("null") : builder.append(o.toString());
    }
}
