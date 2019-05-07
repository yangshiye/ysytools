package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

/**
 * Created by hanfeng.sheng on 2017/10/30.
 */
public class StringBufferSerializerFactory extends SerializerFactory {
    //
//    public static void main(String[] args) {
//        StringBuffer sBuffer = new StringBuffer("defg");
//        StringBufferSerializerFactory strFactory = new StringBufferSerializerFactory();
//        Output output = new Output(1024);
//        strFactory.writeObj(sBuffer,output,null);
//        Input input = new Input(output.toBytes());
//        StringBuffer s = strFactory.readObj(input,null);
//        System.out.println(s);
//    }
    @Override
    public void doWriteObj(Object obj, Output output, GqSeContext context, int depth) {
        StringBuffer sb = (StringBuffer) obj;
        output.writeString(sb);
    }

    @Override
    public StringBuffer readObj(Input input, GqSeContext context) {
        StringBuffer buffer = new StringBuffer(input.readString());
        return buffer;
    }
}
