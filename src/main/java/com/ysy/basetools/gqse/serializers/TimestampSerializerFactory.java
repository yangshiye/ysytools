package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

import java.sql.Timestamp;

public class TimestampSerializerFactory extends SerializerFactory<Timestamp> {
//    public static void main(String[] args) {
//        TimestampSerializerFactory timestampFactory = new TimestampSerializerFactory();
//        Output out = new Output(2048);
//        Timestamp ts = Timestamp.valueOf("2017-10-31 11:49:45");
//        System.out.println(ts);
//        timestampFactory.writeObj(ts, out, null);
//        Input in = new Input(out.toBytes());
//        System.out.println(Arrays.toString(out.toBytes()));
//        System.out.println(timestampFactory.readObj(in,null));
//    }

    @Override
    public void doWriteObj(Timestamp obj, Output output, GqSeContext context, int depth) {
        output.writeLong(obj.getTime());
        output.writeInt(obj.getNanos(), true);
    }

    @Override
    public Timestamp readObj(Input input, GqSeContext context) {
        long time = input.readLong();
        int na = input.readInt(true);
        Timestamp times = new Timestamp(time);
        times.setNanos(na);
        return times;
    }
}
