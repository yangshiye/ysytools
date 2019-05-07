package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSe;
import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by hanfeng.sheng on 2017/10/30.
 */
public class HashSetSerializerFactory extends SerializerFactory<HashSet<?>> {
    private final GqSe gqSe;

    public HashSetSerializerFactory(GqSe gqSe) {
        this.gqSe = gqSe;
    }

    @Override
    public void doWriteObj(HashSet<?> obj, Output output, GqSeContext context, int depth) {
        output.writeInt(obj.size(), true);
        Iterator itor = obj.iterator();
        while (itor.hasNext()) {
            gqSe.writeObj(itor.next(), output, context, depth + 1);
        }
    }

    @Override
    public HashSet<?> readObj(Input input, GqSeContext context) {
        int len = input.readInt(true);
        HashSet<Object> set = new HashSet<Object>(len);
        for (int i = 0; i < len; i++) {
            set.add(gqSe.deserialize(input, context));
        }
        return set;
    }
}
