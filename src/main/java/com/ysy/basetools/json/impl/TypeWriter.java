package com.ysy.basetools.json.impl;

import com.ysy.basetools.json.JSONWriter;
import com.ysy.basetools.json.JSONWriterUtil;
import com.ysy.basetools.json.model.ReflectMap;

import java.lang.reflect.Type;

/**
 * Created by guoqiang on 2017/1/3.
 */
public class TypeWriter implements JSONWriter {
    @Override
    public Class getFormatClass() {
        return Type.class;
    }

    @Override
    public boolean isFormat(Object o) {
        return o instanceof Type;
    }

    @Override
    public StringBuilder getJSONStr(Object o, StringBuilder builder, String path, ReflectMap map) {
        return JSONWriterUtil.toJSONStr(String.valueOf(o), builder, path, map);
    }
}
