package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

import java.util.UUID;

/**
 * @author yanlin.hou@ucarinc.com
 * @description:
 * @className: UuidSerializerFactory
 * @date October 30,2017
 */
public class UuidSerializerFactory extends SerializerFactory<UUID> {

    @Override
    public void doWriteObj(UUID obj, Output output, GqSeContext context, int depth) {
        long mostSigBits = obj.getMostSignificantBits();
        output.writeLong(mostSigBits, true);
        long leastSigBits = obj.getLeastSignificantBits();
        output.writeLong(leastSigBits, true);
    }

    @Override
    public UUID readObj(Input input, GqSeContext context) {
        long mostSigBits = input.readLong(true);
        long leastSigBits = input.readLong(true);
        return new UUID(mostSigBits, leastSigBits);
    }
//
//    public static void main(String[] args) {
//        UUID uuid = UUID.randomUUID();
//        System.out.println(uuid);
//
//        UuidSerializerFactory uuidSerializerFactory = new UuidSerializerFactory();
//        Output out = new Output(2048);
//        uuidSerializerFactory.writeObj(uuid, out, null);
//        Input in = new Input(out.toBytes());
//        uuid = uuidSerializerFactory.readObj(in, null);
//        System.out.println(uuid);
//    }
}
