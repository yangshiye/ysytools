package com.ysy.basetools.json.impl;

import com.ysy.basetools.json.JSONWriter;
import com.ysy.basetools.json.JSONWriterUtil;
import com.ysy.basetools.json.model.ReflectMap;

import java.lang.reflect.Field;

/**
 * Created by guoqiang on 2017/1/3.
 */
public class FieldWriter implements JSONWriter {
    @Override
    public Class getFormatClass() {
        return Field.class;
    }

    @Override
    public boolean isFormat(Object o) {
        return o instanceof Field;
    }

    @Override
    public StringBuilder getJSONStr(Object o, StringBuilder builder, String path, ReflectMap map) {
        return JSONWriterUtil.toJSONStr(String.valueOf(o), builder, path, map);
    }
}
