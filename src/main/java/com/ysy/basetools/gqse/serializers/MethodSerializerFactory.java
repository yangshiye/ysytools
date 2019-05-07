package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSe;
import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

import java.lang.reflect.Method;

public class MethodSerializerFactory extends SerializerFactory<Method> {
    private final GqSe gqSe;

    public MethodSerializerFactory(GqSe gqSe) {
        this.gqSe = gqSe;
    }

    /**
     * @param obj
     * @param output
     * @param context split(分隔符):返回一个字符数组，给定字符串把原串分开
     *                substring（）：
     *                toGenericString()：已经把参数类型取出且转为String
     */
    @Override
    public void doWriteObj(Method obj, Output output, GqSeContext context, int depth) {//要把方法所在的对象，方法名，方法参数写进去
        String methodName = obj.getName();//获取方法名
        Class methodClass = obj.getDeclaringClass();//获取方法所在的类  ;getName()类转为String
        Class[] types = obj.getParameterTypes();//返回参数类型数组
        context.writeClass(output, methodClass);
        output.writeString(methodName);
        if (types == null) {
            types = new Class[0];
        }
        output.writeInt(types.length, true);
        for (int i = 0; i < types.length; i++) {
            context.writeClass(output, types[i]);
        }
    }

    @Override
    public Method readObj(Input input, GqSeContext context) {//input里面的相当于字符串
        Class clazz = context.readClass(input, gqSe);
        String methodName = input.readString();
        int len = input.readInt(true);
        Class params[] = new Class[len];
        for (int i = 0; i < len; i++) {
            params[i] = context.readClass(input, gqSe);
        }
        try {
            return clazz.getDeclaredMethod(methodName, params);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("class=" + clazz + ",name=" + methodName + ",len=" + len, e);
        }
    }
}