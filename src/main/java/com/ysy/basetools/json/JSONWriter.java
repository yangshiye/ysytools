package com.ysy.basetools.json;


import com.ysy.basetools.json.model.ReflectMap;

/**
 * Created by guoqiang on 2016/10/25.
 */
public interface JSONWriter {
    Class getFormatClass();

    boolean isFormat(Object o);

    StringBuilder getJSONStr(Object o, StringBuilder builder, String path, ReflectMap map);
}
