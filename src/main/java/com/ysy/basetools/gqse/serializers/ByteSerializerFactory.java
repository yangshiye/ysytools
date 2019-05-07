package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

/**
 * Created by guoqiang on 2017/10/25.
 */
public class ByteSerializerFactory extends SerializerFactory<Byte> {
    @Override
    public void doWriteObj(Byte obj, Output output, GqSeContext context, int depth) {
        output.writeByte(obj);
    }

    @Override
    public Byte readObj(Input input, GqSeContext context) {
        return input.readByte();
    }
}
