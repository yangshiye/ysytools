package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

/**
 * @author yanlin.hou@ucarinc.com
 * @description:
 * @className: DoubleSerializerFactory
 * @date October 25,2017
 */
public class DoubleSerializerFactory extends SerializerFactory<Double> {
//
//    public static void main(String[] args) {
//        DoubleSerializerFactory s = new DoubleSerializerFactory();
//        Output out = new Output();
//        double d = 11.2;
//        s.writeObj(d, out, null);
//        Input in = new Input(out.toBytes());
//        System.out.println(s.readObj(in, null));
//    }

    @Override
    public void doWriteObj(Double obj, Output output, GqSeContext context, int depth) {
        output.writeDouble(obj);
    }

    @Override
    public Double readObj(Input input, GqSeContext context) {
        return input.readDouble();
    }
}
