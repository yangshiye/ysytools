package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

/**
 * Created by hanfeng.sheng on 2017/10/30.
 */
public class StringBuilderSerializerFactory extends SerializerFactory {
    //
//    public static void main(String[] args) {
//        StringBuilderSerializerFactory factory = new StringBuilderSerializerFactory();
//        Output out = new Output(1024);
//        StringBuilder sb = new StringBuilder("abcd");
//        factory.writeObj(sb,out,null);
//        Input input = new Input(out.toBytes());
//        StringBuilder res = factory.readObj(input,null);
//        System.out.println(res);
//    }
    @Override
    public void doWriteObj(Object obj, Output output, GqSeContext context, int depth) {
        StringBuilder sb = (StringBuilder) obj;
        output.writeString(sb);
    }

    @Override
    public StringBuilder readObj(Input input, GqSeContext context) {
        StringBuilder sb = new StringBuilder(input.readString());
        return sb;
    }
}
