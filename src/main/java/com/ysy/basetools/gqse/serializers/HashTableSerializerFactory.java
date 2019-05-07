package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSe;
import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by hanfeng.sheng on 2017/10/31.
 */
public class HashTableSerializerFactory extends SerializerFactory<Hashtable<?, ?>> {
    private final GqSe gqSe;

    public HashTableSerializerFactory(GqSe gqSe) {
        this.gqSe = gqSe;
    }

    @Override
    public void doWriteObj(Hashtable<?, ?> obj, Output output, GqSeContext context, int depth) {
        output.writeInt(obj.size(), true);
        Iterator itor = obj.entrySet().iterator();
        while (itor.hasNext()) {
            Map.Entry entry = (Map.Entry) itor.next();
            gqSe.writeObj(entry.getKey(), output, context, depth + 1);
            gqSe.writeObj(entry.getValue(), output, context, depth + 1);
        }
    }

    @Override
    public Hashtable<?, ?> readObj(Input input, GqSeContext context) {
        int len = input.readInt(true);
        Hashtable<Object, Object> hashtable = new Hashtable<Object, Object>(len);
        for (int i = 0; i < len; i++) {
            Object key = gqSe.deserialize(input, context);
            Object value = gqSe.deserialize(input, context);
            hashtable.put(key, value);
        }
        return hashtable;
    }
}
