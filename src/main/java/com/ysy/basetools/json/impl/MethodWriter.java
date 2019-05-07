package com.ysy.basetools.json.impl;

import com.ysy.basetools.json.JSONWriter;
import com.ysy.basetools.json.JSONWriterUtil;
import com.ysy.basetools.json.model.ReflectMap;

import java.lang.reflect.Method;

/**
 * Created by guoqiang on 2017/1/3.
 */
public class MethodWriter implements JSONWriter {
    @Override
    public Class getFormatClass() {
        return Method.class;
    }

    @Override
    public boolean isFormat(Object o) {
        return o instanceof Method;
    }

    @Override
    public StringBuilder getJSONStr(Object o, StringBuilder builder, String path, ReflectMap map) {
        return JSONWriterUtil.toJSONStr(String.valueOf(o), builder, path, map);
    }
}
