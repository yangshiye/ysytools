package com.ysy.basetools.json.impl;


import com.ysy.basetools.json.JSONWriter;
import com.ysy.basetools.json.model.ReflectMap;

/**
 * Created by guoqiang on 2016/11/25.
 */
public class StackTraceElementWriter implements JSONWriter {
    @Override
    public Class getFormatClass() {
        return StackTraceElement.class;
    }

    @Override
    public boolean isFormat(Object o) {
        return o instanceof StackTraceElement;
    }

    @Override
    public StringBuilder getJSONStr(Object o, StringBuilder builder, String path, ReflectMap map) {
        if (o == null) {
            return builder.append("null");
        }
        if (o instanceof StackTraceElement) {
            builder.append("\"").append(o.toString()).append("\"");
        } else {
            builder.append("\"").append("@@@error class").append("\"");
        }
        return builder;
    }
}
