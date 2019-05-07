package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

/**
 * Created by changpan on 2017/10/30.
 */
public class StackTraceElementSerializerFactory extends SerializerFactory<StackTraceElement> {

    @Override
    public void doWriteObj(StackTraceElement obj, Output output, GqSeContext context, int depth) {
        output.writeString(obj.getClassName());
        output.writeString(obj.getMethodName());
        output.writeString(obj.getFileName());
        output.writeInt(obj.getLineNumber(), true);
    }

    @Override
    public StackTraceElement readObj(Input input, GqSeContext context) {
        StackTraceElement ste = new StackTraceElement(input.readString(),
                input.readString(), input.readString(), input.readInt(true));
        return ste;
    }

    public StackTraceElementSerializerFactory() {
    }
//
//    public static void main(String[] args) {
//        StackTraceElementSerializerFactory sSerializerFactory = new StackTraceElementSerializerFactory();
//        StackTraceElement ste = new StackTraceElement("class",
//                "method", "file", 111);
//        Output output = new Output(1024);
//        sSerializerFactory.writeObj(ste,output,null);
//        Input input = new Input(output.toBytes());
//        ste = sSerializerFactory.readObj(input,null);
//        System.out.println(ste);
//    }
}
