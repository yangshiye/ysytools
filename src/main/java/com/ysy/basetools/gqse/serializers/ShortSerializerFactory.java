package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

/**
 * @author yanlin.hou@ucarinc.com
 * @description:
 * @className: ShortSerializerFactory
 * @date October 25,2017
 */
public class ShortSerializerFactory extends SerializerFactory<Short> {
//
//    public static void main(String[] args) {
//        ShortSerializerFactory s = new ShortSerializerFactory();
//        Output out = new Output();
//        short a = 129;
//        s.writeObj(a, out, null);
//        Input in = new Input(out.toBytes());
//        System.out.println(s.readObj(in, null));
//    }

    @Override
    public void doWriteObj(Short obj, Output output, GqSeContext context, int depth) {
        output.writeShort(obj);
    }

    @Override
    public Short readObj(Input input, GqSeContext context) {
        return input.readShort();
    }
}
