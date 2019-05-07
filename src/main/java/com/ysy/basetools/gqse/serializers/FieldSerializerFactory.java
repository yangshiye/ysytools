package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSe;
import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

import java.lang.reflect.Field;

public class FieldSerializerFactory extends SerializerFactory<Field> {

    private final GqSe gqSe;

    public FieldSerializerFactory(GqSe gqSe) {
        this.gqSe = gqSe;
    }//Field类描述的是 类的属性全部信息

    @Override
    public void doWriteObj(Field obj, Output output, GqSeContext context, int depth) {
        context.writeClass(output, obj.getDeclaringClass());
        output.writeString(obj.getName());
    }

    @Override
    public Field readObj(Input input, GqSeContext context) {
        Class clazz = context.readClass(input, gqSe);
        String name = input.readString();
        try {
            return clazz.getDeclaredField(name);
        } catch (Throwable e) {
            throw new RuntimeException("clazz=" + clazz + ",name=" + name, e);
        }
    }
}
