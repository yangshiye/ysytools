package com.ysy.basetools.json.impl;

import com.ysy.basetools.json.JSONWriter;
import com.ysy.basetools.json.JSONWriterUtil;
import com.ysy.basetools.json.model.ReflectMap;
import com.ysy.basetools.log.LogUtil;
import com.ysy.basetools.util.ListUtil;
import com.ysy.basetools.util.ReflectUtil;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by guoqiang on 2016/10/25.
 * OK.
 */
public class ReflectWriter implements JSONWriter {
    private static final Logger logger = LogUtil.LOG;

    @Override
    public Class getFormatClass() {
        return Object.class;
    }

    @Override
    public boolean isFormat(Object o) {
        return true;
    }

    @Override
    public StringBuilder getJSONStr(Object o, StringBuilder builder, String path, ReflectMap map) {
        if (o == null) {
            return builder.append("null");
        }
        Class<?> clazz = o.getClass();
        List<Field> fields = new LinkedList<Field>();
        for (Field field : ReflectUtil.getAllIFieldList(clazz)) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            fields.add(field);
        }
        if (ListUtil.isEmpty(fields)) {
            return builder.append("{}");
        }
        builder.append("{");
        Iterator<Field> it = fields.iterator();
        boolean NFirst = false;
        while (it.hasNext()) {
            Field field = it.next();
            Object obj;
            try {
                obj = ReflectUtil.getFieldValue(o, field);
            } catch (Exception e) {
                logger.error("field getValue error =" + field, e);
                break;
            }
            if (obj != null) {
                if (NFirst) {
                    builder.append(",");
                }
                String name = field.getName();
                String curPath = JSONWriterUtil.getPath(path, "{" + name + "}");
                builder.append("\"").append(name).append("\":");
                JSONWriterUtil.toJSONStr(obj, builder, curPath, map);
                NFirst = true;
            }
        }
        builder.append("}");
        return builder;
    }
}
