package com.ysy.basetools.json.impl;


import com.ysy.basetools.json.JSONWriter;
import com.ysy.basetools.json.model.ReflectMap;
import com.ysy.basetools.util.StrUtil;

/**
 * Created by guoqiang on 2016/11/25.
 */
public class ThrowableWriter implements JSONWriter {
    @Override
    public Class getFormatClass() {
        return Throwable.class;
    }

    @Override
    public boolean isFormat(Object o) {
        return o instanceof Throwable;
    }

    @Override
    public StringBuilder getJSONStr(Object o, StringBuilder builder, String path, ReflectMap map) {
        if (o == null) {
            return builder.append("null");
        }
        if (o instanceof Throwable) {
            builder.append("\"").append(StrUtil.toString(o)).append("\"");
        } else {
            builder.append("\"").append("@@@error class").append("\"");
        }
        return builder;
    }
}
