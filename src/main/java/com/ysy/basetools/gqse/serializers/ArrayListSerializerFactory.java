package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSe;
import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

import java.util.ArrayList;


/**
 * Created by lixiaojian on 2017/10/30.
 */
public class ArrayListSerializerFactory<T> extends SerializerFactory<ArrayList<T>> {
    private final GqSe gqse;

    @Override
    public void doWriteObj(ArrayList<T> obj, Output output, GqSeContext context, int depth) {
        int size = obj.size();
        output.writeInt(size, true);
        for (T t : obj) {
            gqse.writeObj(t, output, context, depth + 1);
        }
    }

    @Override
    public ArrayList<T> readObj(Input input, GqSeContext context) {
        ArrayList<T> res = new ArrayList<T>();
        int len = input.readInt(true);
        for (int i = 0; i < len; i++) {
            T t = gqse.deserialize(input, context);
            res.add(t);
        }
        return res;
    }

    public ArrayListSerializerFactory(GqSe gqse) {
        this.gqse = gqse;
    }
}
