package com.ysy.basetools.gqse.serializers;


import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

public class IntSerializerFactory extends SerializerFactory<Integer> {
//
//    public static void main(String args[]){
//        IntSerializerFactory i = new IntSerializerFactory();//int类型的序列化工厂对象
//        Output out=new Output(4096);
//        i.writeObj(-123434,out,null);
//        Input in= new Input(out.toBytes());
//        System.out.println(Arrays.toString(out.toBytes()));
//        System.out.println(i.readObj(in,null));
//    }

    @Override
    public void doWriteObj(Integer obj, Output output, GqSeContext context, int depth) {
        output.writeInt(obj, true);
    }

    @Override
    public Integer readObj(Input input, GqSeContext context) {
        return input.readInt(true);

    }

}
