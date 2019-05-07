package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

import java.math.BigInteger;

/**
 * @author yanlin.hou@ucarinc.com
 * @description:
 * @className: BigIntegerSerializerFactory
 * @date October 30,2017
 */
public class BigIntegerSerializerFactory extends SerializerFactory<BigInteger> {

    @Override
    public void doWriteObj(BigInteger obj, Output output, GqSeContext context, int depth) {
        byte[] bytes = obj.toByteArray();
        output.writeByteArr(bytes);
    }

    @Override
    public BigInteger readObj(Input input, GqSeContext context) {
        byte[] bytes = input.readByteArr();
        return new BigInteger(bytes);
    }
//
//    public static void main(String[] args) {
//        BigInteger b = new BigInteger("46690877343413");
//        System.out.println(b);
//
//        BigIntegerSerializerFactory bigIntegerSerializerFactory = new BigIntegerSerializerFactory();
//        Output out = new Output(2048);
//        bigIntegerSerializerFactory.writeObj(b, out, null);
//        Input in = new Input(out.toBytes());
//        b = bigIntegerSerializerFactory.readObj(in, null);
//        System.out.println(b);
//    }
}
