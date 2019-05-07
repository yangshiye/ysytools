package com.ysy.basetools.json.impl;

import com.ysy.basetools.json.JSONWriter;
import com.ysy.basetools.json.JSONWriterUtil;
import com.ysy.basetools.json.model.ReflectMap;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by guoqiang on 2016/10/25.
 * OK
 */
public class CollectionWriter implements JSONWriter {
    @Override
    public Class getFormatClass() {
        return Collection.class;
    }

    @Override
    public boolean isFormat(Object o) {
        return o instanceof Collection;
    }

    @Override
    public StringBuilder getJSONStr(Object o, StringBuilder builder, String path, ReflectMap map) {
        if (o == null) {
            return builder.append("null");
        }
        if (o instanceof Collection) {
            Collection list = (Collection) o;
            if (list.isEmpty()) {
                return builder.append("[]");
            } else {
                builder.append("[");
                Iterator it = list.iterator();
                int idx = 0;
                while (it.hasNext()) {
                    Object obj = it.next();
                    String curPath = JSONWriterUtil.getPath(path, "[" + idx + "]");
                    JSONWriterUtil.toJSONStr(obj, builder, curPath, map);//TODO
                    if (it.hasNext()) {
                        builder.append(",");
                    }
                    idx++;
                }
                builder.append("]");
                return builder;
            }
        }
        return builder;
    }
}
