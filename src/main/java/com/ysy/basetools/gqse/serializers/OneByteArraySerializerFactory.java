package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

/**
 * Created by guoqiang on 2017/10/27.
 */
public class OneByteArraySerializerFactory extends SerializerFactory<byte[]> {

    @Override
    public void doWriteObj(byte[] obj, Output output, GqSeContext context, int depth) {
        output.writeByteArr(obj);
    }

    @Override
    public byte[] readObj(Input input, GqSeContext context) {
        return input.readByteArr();
    }

}
