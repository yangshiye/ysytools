package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSe;
import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hanfeng.sheng on 2017/11/1.
 */
public class ConcurrentHashMapSerializerFactory extends SerializerFactory<ConcurrentHashMap<?, ?>> {
    private final GqSe gqSe;

    public ConcurrentHashMapSerializerFactory(GqSe gqSe) {
        this.gqSe = gqSe;
    }

    @Override
    public void doWriteObj(ConcurrentHashMap<?, ?> obj, Output output, GqSeContext context, int depth) {
        output.writeInt(obj.size(), true);
        Iterator itor = obj.entrySet().iterator();
        while (itor.hasNext()) {
            ConcurrentHashMap.Entry entry = (ConcurrentHashMap.Entry) itor.next();
            gqSe.writeObj(entry.getKey(), output, context, depth + 1);
            gqSe.writeObj(entry.getValue(), output, context, depth + 1);
        }
    }

    @Override
    public ConcurrentHashMap<?, ?> readObj(Input input, GqSeContext context) {
        int len = input.readInt(true);
        ConcurrentHashMap<Object, Object> concurrentHashMap = new ConcurrentHashMap<Object, Object>(len);
        for (int i = 0; i < len; i++) {
            Object key = gqSe.deserialize(input, context);
            Object val = gqSe.deserialize(input, context);
            concurrentHashMap.putIfAbsent(key, val);
        }
        return concurrentHashMap;
    }
}
