package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSe;
import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

import java.util.LinkedList;


/**
 * Created by lixiaojian on 2017/10/30.
 */
public class LinkedListSerializerFactory<T> extends SerializerFactory<LinkedList<T>> {
    private final GqSe gqse;

    @Override
    public void doWriteObj(LinkedList<T> obj, Output output, GqSeContext context, int depth) {
        int size = obj.size();
        output.writeInt(size, true);
        for (T t : obj) {
            gqse.writeObj(t, output, context, depth + 1);
        }
    }

    @Override
    public LinkedList<T> readObj(Input input, GqSeContext context) {
        LinkedList<T> res = new LinkedList<T>();
        int len = input.readInt(true);
        for (int i = 0; i < len; i++) {
            T t = gqse.deserialize(input, context);
            res.add(t);
        }
        return res;
    }

    public LinkedListSerializerFactory(GqSe gqse) {
        this.gqse = gqse;
    }
}
