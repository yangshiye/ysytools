package com.ysy.basetools.json;

import com.ysy.basetools.json.impl.*;
import com.ysy.basetools.json.model.ReflectMap;
import com.ysy.basetools.json.model.ReflectModel;
import com.ysy.basetools.util.StrUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by guoqiang on 2016/10/25.
 */
public class JSONWriterUtil {
    private static final Map<Class, JSONWriter> writerMap = new HashMap<Class, JSONWriter>();
    private static final int DEFAULT_SIZE = 1024;
    private static final String ROOT_PATH = "@";
    private static final JSONWriter defaultWriter = new ReflectWriter();
    private static final JSONWriter arrayWriter = new ArrayWriter();
    private static final JSONWriter stringWriter = new StringWriter();
    private static final JSONWriter numberWriter = new NumberWriter();
    private static final JSONWriter dateWriter = new DateWriter();
    private static final JSONWriter mapWriter = new MapWriter();
    private static final JSONWriter collectionWriter = new CollectionWriter();

    public static String toJSONStr(Object o) {
        if (o == null) {
            return "null";
        }
        return toJSONStr(o, new StringBuilder(DEFAULT_SIZE), ROOT_PATH, new ReflectMap()).toString();
    }

    public static StringBuilder toJSONStr(Object o, StringBuilder builder) {
        return toJSONStr(o, builder, ROOT_PATH, new ReflectMap());
    }

    public static StringBuilder toJSONStr(Object o, StringBuilder builder, String path,
                                          ReflectMap map) {
        try {
            if (builder == null) {
                builder = new StringBuilder(DEFAULT_SIZE);
            }
            if (o == null) {
                return builder.append("null");
            }
            ReflectModel model = map.get(o);
            if (model != null) {
                StringBuilder result = stringWriter.getJSONStr("$ref_" + model.getPath(), builder, path, map);
                return result;
            } else {
                JSONWriter writer = getJSONWriterByObj(o);
                map.putPath(o, path);
                StringBuilder result = writer.getJSONStr(o, builder, path, map);
                map.remove(o);
                return result;
            }
        } catch (Exception e) {
            return new StringBuilder("json error!");
        }
    }

    private static JSONWriter getJSONWriterByObj(Object o) {
        if (o instanceof Map) {
            return mapWriter;
        } else if (o instanceof Collection) {
            return collectionWriter;
        } else if (o instanceof Number) {
            return numberWriter;
        }
        Class oc = o.getClass();
        if (oc.isArray()) {
            return arrayWriter;
        }
        JSONWriter writer = writerMap.get(oc);
        if (writer == null) {
            for (Class<?> clazz : writerMap.keySet()) {
                if (clazz.isAssignableFrom(oc)) {
                    writer = writerMap.get(clazz);
                    break;
                }
            }
        }
        if (writer == null) {
            writer = defaultWriter;
        }
        return writer;
    }

    public static String getPath(String parent, String cur) {
        if (StrUtil.isEmpty(parent)) {
            return cur;
        } else {
            return parent + "." + cur;
        }
    }

    static {
        writerMap.put(Byte.class, numberWriter);
        writerMap.put(Number.class, numberWriter);
        writerMap.put(BigDecimal.class, numberWriter);
        writerMap.put(BigInteger.class, numberWriter);
        writerMap.put(Short.class, numberWriter);
        writerMap.put(Integer.class, numberWriter);
        writerMap.put(Long.class, numberWriter);
        writerMap.put(Character.class, stringWriter);
        writerMap.put(Boolean.class, new BooleanWriter());
        writerMap.put(Float.class, numberWriter);
        writerMap.put(Double.class, numberWriter);
        writerMap.put(Collection.class, collectionWriter);
        writerMap.put(Map.class, new MapWriter());
        writerMap.put(String.class, stringWriter);
        writerMap.put(CharSequence.class, stringWriter);
        writerMap.put(StringBuilder.class, stringWriter);
        writerMap.put(StringBuffer.class, stringWriter);
        writerMap.put(Date.class, dateWriter);
        writerMap.put(java.sql.Date.class, dateWriter);
        writerMap.put(java.sql.Timestamp.class, dateWriter);
        writerMap.put(Throwable.class, new ThrowableWriter());
        writerMap.put(StackTraceElement.class, new StackTraceElementWriter());
        writerMap.put(Type.class, new TypeWriter());
        writerMap.put(Field.class, new FieldWriter());
        writerMap.put(Method.class, new MethodWriter());
    }
}
