package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSe;
import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;


public class ClassSerializerFactory extends SerializerFactory<Class> {
    private final GqSe gqSe;

    @Override
    public void doWriteObj(Class obj, Output output, GqSeContext context, int depth) {
        context.writeClass(output, obj);
    }

    @Override
    public Class readObj(Input input, GqSeContext context) {
        return context.readClass(input, gqSe);
    }

    public ClassSerializerFactory(GqSe gqSe) {
        this.gqSe = gqSe;
    }

}
