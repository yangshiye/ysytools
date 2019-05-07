package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

/**
 * Created by guoqiang on 2017/10/25.
 */
public class FloatSerializerFactory extends SerializerFactory<Float> {
    @Override
    public void doWriteObj(Float obj, Output output, GqSeContext context, int depth) {
        output.writeFloat(obj);
    }

    @Override
    public Float readObj(Input input, GqSeContext context) {
        return input.readFloat();
    }
}
