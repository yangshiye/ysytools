package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

/**
 * Created by guoqiang on 2017/10/25.
 */
public class StrSerializerFactory extends SerializerFactory<String> {
    @Override
    public void doWriteObj(String obj, Output output, GqSeContext context, int depth) {
        output.writeString(obj);
    }

    @Override
    public String readObj(Input input, GqSeContext context) {
        return input.readString();
    }
}
