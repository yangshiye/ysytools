package com.ysy.basetools.gqse.serializers;//package com.ysy.basetools.gqse.serializers;
//
//import com.ysy.basetools.gqse.GqSeContext;
//import com.ysy.basetools.gqse.io.Input;
//import com.ysy.basetools.gqse.io.Output;
//
///**
// * Created by guoqiang on 2017/11/2.
// */
//public class RenSerializerFactory extends SerializerFactory {
//
//    @Override
//    public void doWriteObj(Object obj, Output output, GqSeContext context, int depth) {
//        int id = context.putNextObj(obj);
//        output.writeInt(id, true);
//    }
//
//    @Override
//    public Object readObj(Input input, GqSeContext context) {
//        Object obj = context.getObj(input.readInt(true));
//        return obj;
//    }
//
//}
