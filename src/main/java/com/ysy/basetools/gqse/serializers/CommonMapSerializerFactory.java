package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSe;
import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.GqSeImpl;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by guoqiang on 2017/10/27.
 */
public class CommonMapSerializerFactory extends SerializerFactory<Map<?, ?>> {
    private final GqSe gqSe;
    private static final String UnmodifiableMap = "java.util.Collections$UnmodifiableMap";
    private static final String UnmodifiableSortedMap = "java.util.Collections$UnmodifiableSortedMap";
    private static final String SynchronizedMap = "java.util.Collections$SynchronizedMap";
    private static final String SynchronizedSortedMap = "java.util.Collections$SynchronizedSortedMap";

    @Override
    public void doWriteObj(Map<?, ?> obj, Output output, GqSeContext context, int depth) {
        context.writeClass(output, obj.getClass());
        output.writeInt(obj.size(), true);
        for (Map.Entry temp : obj.entrySet()) {
            gqSe.writeObj(temp.getKey(), output, context, depth + 1);
            gqSe.writeObj(temp.getValue(), output, context, depth + 1);
        }
    }

    @Override
    public Map<?, ?> readObj(Input input, GqSeContext context) {
        Class clazz = context.readClass(input, gqSe);
        int len = input.readInt(true);
        Map map = createMap(clazz, context, len);
        for (int i = 0; i < len; i++) {
            Object key = gqSe.deserialize(input, context);
            Object value = gqSe.deserialize(input, context);
            map.put(key, value);
        }
        return getRealMap(map, clazz);
    }

    private Map createMap(Class clazz, GqSeContext context, int len) {
        Map map;
        if (UnmodifiableMap.equals(clazz.getName())) {
            map = new HashMap(len);
        } else if (UnmodifiableSortedMap.equals(clazz.getName())) {
            map = new TreeMap();
        } else if (SynchronizedMap.equals(clazz.getName())) {
            map = new HashMap(len);
        } else if (SynchronizedSortedMap.equals(clazz.getName())) {
            map = new TreeMap();
        } else {
            map = gqSe.createObj(clazz, context);
        }

        return map;
    }

    private Map getRealMap(Map map, Class clazz) {
        if (UnmodifiableMap.equals(clazz.getName())) {
            map = Collections.unmodifiableMap(map);
        } else if (UnmodifiableSortedMap.equals(clazz.getName())) {
            map = Collections.unmodifiableSortedMap((TreeMap) map);
        } else if (SynchronizedMap.equals(clazz.getName())) {
            map = Collections.synchronizedMap(map);
        } else if (SynchronizedSortedMap.equals(clazz.getName())) {
            map = Collections.synchronizedSortedMap((TreeMap) map);
        }

        return map;
    }

    public CommonMapSerializerFactory(GqSeImpl gqSe) {
        this.gqSe = gqSe;
    }
}
