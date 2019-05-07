package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.AllClassInfo;
import com.ysy.basetools.gqse.GqSe;
import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by guoqiang on 2017/10/20.
 */
public class DefaultSerializerFactory extends SerializerFactory {
    private final ConcurrentHashMap<AllClassInfo, Serializer> map = new ConcurrentHashMap<AllClassInfo, Serializer>(1024);
    private final GqSe gqSe;

    public DefaultSerializerFactory(GqSe gqSe) {
        this.gqSe = gqSe;
    }

    @Override
    public void doWriteObj(Object obj, Output output, GqSeContext context, int depth) {
        Class temp = obj.getClass();
        context.writeClass(output, temp);
        Serializer s = getSerializer(temp);
        s.writeObj(obj, output, context, depth);//这里层数不加1是因为直接给外部进行序列化当前对象不是深入一层
    }

    @Override
    public Object readObj(Input input, GqSeContext context) {
        Class clazz = context.readClass(input, gqSe);
        Serializer s = getSerializer(clazz);
        return s.readObj(input, context);
    }

    private Serializer getSerializer(Class clazz) {
        AllClassInfo classInfo = new AllClassInfo(clazz);
        Serializer s = map.get(classInfo);
        if (s == null) {
            synchronized (clazz) {
                s = map.get(classInfo);
                if (s == null) {
                    s = new CommonSerializer(gqSe, clazz);
                    map.put(classInfo, s);
                }
            }
        }
        return s;
    }
}
