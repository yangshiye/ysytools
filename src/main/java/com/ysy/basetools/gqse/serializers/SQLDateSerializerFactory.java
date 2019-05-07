package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

public class SQLDateSerializerFactory extends SerializerFactory<java.sql.Date> {
//    public static void main(String[] args) {
//        SQLDateSerializerFactory sqlDateFactory = new SQLDateSerializerFactory();
//        Output out = new Output(2048);
//        java.sql.Date sqlDate = new Date(System.currentTimeMillis());
//        System.out.println("orgDate=" + TimeUtil.formatAllTime(sqlDate));
//        sqlDateFactory.writeObj(sqlDate, out, null);
//        Input in = new Input(out.toBytes());
//        System.out.println(Arrays.toString(out.toBytes()));
//        System.out.println("desDate=" + TimeUtil.formatAllTime(sqlDateFactory.readObj(in, null)));
//
//    }

    @Override
    public void doWriteObj(java.sql.Date obj, Output output, GqSeContext context, int depth) {
        output.writeLong(obj.getTime());
    }

    @Override
    public java.sql.Date readObj(Input input, GqSeContext context) {
        return new java.sql.Date(input.readLong());
    }

}
