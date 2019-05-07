package com.ysy.basetools.json.impl;

import com.ysy.basetools.json.JSONWriter;
import com.ysy.basetools.json.JSONWriterUtil;
import com.ysy.basetools.json.model.ReflectMap;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by guoqiang on 2016/10/25.
 * OK.
 */
public class MapWriter implements JSONWriter {
    @Override
    public Class getFormatClass() {
        return Map.class;
    }

    @Override
    public boolean isFormat(Object o) {
        return o instanceof Map;
    }

    @Override
    public StringBuilder getJSONStr(Object o, StringBuilder builder, String path, ReflectMap reflectMap) {
        if (o instanceof Map) {
            Map map = (Map) o;
            if (map.isEmpty()) {
                return builder.append("{}");
            } else {
                builder.append("{");
                Iterator it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Object entry = it.next();
                    Object key = ((Map.Entry) entry).getKey();
                    Object value = ((Map.Entry) entry).getValue();
                    if (value != null) {
                        String keyStr = JSONWriterUtil.toJSONStr(key, null, path, reflectMap).toString();
                        builder.append(keyStr).append(":");
                        String curPath = JSONWriterUtil.getPath(path, "{" + keyStr + "}");
                        JSONWriterUtil.toJSONStr(value, builder, curPath, reflectMap);
                        if (it.hasNext()) {
                            builder.append(",");
                        }
                    }
                }
                builder.append("}");
                return builder;
            }
        } else {
            return builder.append("null");
        }
    }
}
