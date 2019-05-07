package com.ysy.basetools.gqse.creators;

import com.ysy.basetools.gqse.AllClassInfo;
import com.ysy.basetools.gqse.GqSeContext;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by guoqiang on 2017/10/25.
 */
public class CommonCreatorImpl implements Creator {
    private static final ReflectionFactory REFLECTION_FACTORY = ReflectionFactory.getReflectionFactory();
    private final ConcurrentHashMap<AllClassInfo, Constructor> constructorMap = new ConcurrentHashMap<AllClassInfo, Constructor>();

    @Override
    public Object create(Class type, GqSeContext context) {
        AllClassInfo classInfo=new AllClassInfo(type);
        Constructor constructor = constructorMap.get(classInfo);
        if (constructor != null) {
            try {
                return constructor.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("type=" + type + ",is can not create", e);
            }
        } else {
            try {
                return type.newInstance();
            } catch (Exception e) {
                constructor = getConstructor(type, context);
                try {
                    return constructor.newInstance();
                } catch (Exception e1) {
                    throw new RuntimeException("type=" + type + ",is can not create", e);
                }
            }
        }
    }

    private Constructor getConstructor(Class type, GqSeContext context) {
        AllClassInfo classInfo=new AllClassInfo(type);
        Constructor constructor = constructorMap.get(classInfo);
        if (constructor == null) {
            synchronized (type) {
                constructor = constructorMap.get(classInfo);
                if (constructor == null) {
                    constructor = newConstructorForSerialization(type);
                    constructorMap.put(classInfo, constructor);
                }
            }
        }
        return constructor;
    }

    private static <T> Constructor<?> newConstructorForSerialization(Class<T> type) {
        try {
            Constructor<?> constructor = REFLECTION_FACTORY.newConstructorForSerialization(type,
                    Object.class.getDeclaredConstructor());
            constructor.setAccessible(true);
            return constructor;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
