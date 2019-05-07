package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

public class BooleanSerializerFactory extends SerializerFactory<Boolean> {
//
//    public static void main(String args[]) {
//        BooleanSerializerFactory i = new BooleanSerializerFactory();//Boolean类型的序列化工厂对象
//        Output out = new Output(4096);
//        i.writeObj(true, out, null);
//        Input in = new Input(out.toBytes());
//        System.out.println(Arrays.toString(out.toBytes()));
//        System.out.println(i.readObj(in, null));
//    }

    @Override
    public void doWriteObj(Boolean obj, Output output, GqSeContext context, int depth) {
        output.writeBoolean(obj);
    }

    @Override
    public Boolean readObj(Input input, GqSeContext context) {
        return input.readBoolean();
    }
}
