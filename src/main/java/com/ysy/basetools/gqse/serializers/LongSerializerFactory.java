package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

/**
 * Created by hanfeng.sheng on 2017/10/24.
 */
public class LongSerializerFactory extends SerializerFactory<Long> {
//
//    public static void main(String[] args) {
//        LongSerializerFactory longFactory = new LongSerializerFactory();
//        Output out = new Output(2048);
//        longFactory.writeObj(5L, out, null);
//        Input in = new Input(out.toBytes());
//        Long l = longFactory.readObj(in, null);
//        System.out.println(l);
//    }

    @Override
    public void doWriteObj(Long obj, Output output, GqSeContext context, int depth) {
        output.writeLong(obj, true);
    }

    @Override
    public Long readObj(Input input, GqSeContext context) {
        return input.readLong(true);
    }
}
