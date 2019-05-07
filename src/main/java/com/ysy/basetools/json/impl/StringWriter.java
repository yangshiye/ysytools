package com.ysy.basetools.json.impl;

import com.ysy.basetools.json.JSONWriter;
import com.ysy.basetools.json.model.ReflectMap;

/**
 * Created by guoqiang on 2016/10/25.
 * OK.
 */
public class StringWriter implements JSONWriter {
    @Override
    public Class getFormatClass() {
        return String.class;
    }

    @Override
    public boolean isFormat(Object o) {
        return o instanceof String || o instanceof StringBuilder || o instanceof StringBuffer
                || o instanceof CharSequence || o instanceof Character;
    }

    @Override
    public StringBuilder getJSONStr(Object o, StringBuilder builder, String path, ReflectMap map) {
        if (o == null) {
            return builder.append("null");
        }
        builder.append("\"");
        for (char c : o.toString().toCharArray()) {
            if (c == '"') {
                builder.append("\\\"");
            } else if (c == '\\') {
                builder.append("\\\\");
            } else if (c == '\n') {
                builder.append("\\n");
            } else if (c == '\t') {
                builder.append("\\t");
            } else if (c == '\r') {
                builder.append("\\r");
            } else {
                builder.append(c);
            }
        }
        builder.append("\"");
        return builder;
    }
}
