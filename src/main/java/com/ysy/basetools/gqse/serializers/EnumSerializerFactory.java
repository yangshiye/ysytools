package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSe;
import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

/**
 * Created by guoqiang on 2017/10/25.
 */
public class EnumSerializerFactory extends SerializerFactory<Enum> {
    private final GqSe gqSe;

    @Override
    public void doWriteObj(Enum obj, Output output, GqSeContext context, int depth) {
        output.writeString(obj.getDeclaringClass().getName());
        output.writeString(obj.name());
    }

    @Override
    public Enum readObj(Input input, GqSeContext context) {
        String className = input.readString();
        String enumName = input.readString();
        try {
            Class clazz = gqSe.getClassByName(className);
            return Enum.valueOf(clazz, enumName);
        } catch (Throwable e) {
            throw new RuntimeException("读取枚举类错误,className=" + className + ",enumName=" + enumName, e);
        }
    }


    public EnumSerializerFactory(GqSe gqSe) {
        this.gqSe = gqSe;
    }

}
