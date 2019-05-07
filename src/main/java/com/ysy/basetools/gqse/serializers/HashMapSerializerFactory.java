package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSe;
import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by hanfeng.sheng on 2017/10/31.
 */
public class HashMapSerializerFactory extends SerializerFactory<HashMap<?, ?>> {
    private final GqSe gqSe;

    public HashMapSerializerFactory(GqSe gqSe) {
        this.gqSe = gqSe;
    }

    @Override
    public void doWriteObj(HashMap<?, ?> obj, Output output, GqSeContext context, int depth) {
        output.writeInt(obj.size(), true);
        Iterator itor = obj.entrySet().iterator();
        while (itor.hasNext()) {
            Map.Entry entry = (Map.Entry) itor.next();
            gqSe.writeObj(entry.getKey(), output, context, depth + 1);
            gqSe.writeObj(entry.getValue(), output, context, depth + 1);
        }
    }

    @Override
    public HashMap<?, ?> readObj(Input input, GqSeContext context) {
        int len = input.readInt(true);
        HashMap<Object, Object> hashMap = new HashMap<Object, Object>(len);
        for (int i = 0; i < len; i++) {
            Object key = gqSe.deserialize(input, context);
            Object val = gqSe.deserialize(input, context);
            hashMap.put(key, val);
        }
        return hashMap;
    }
}
