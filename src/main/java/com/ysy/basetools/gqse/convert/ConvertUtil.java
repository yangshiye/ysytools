package com.ysy.basetools.gqse.convert;


import com.ysy.basetools.gqse.convert.impl.*;
import com.ysy.basetools.gqse.exceptions.GqSeEx;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2018/12/7.
 */
public class ConvertUtil {
    private static final Map<Class, Converter<?>> converterMap = new ConcurrentHashMap<>(64);

    static {
        converterMap.put(char.class, new CharacterConvert());
        converterMap.put(Character.class, new CharacterConvert());

        converterMap.put(boolean.class, new BooleanConvert());
        converterMap.put(Boolean.class, new BooleanConvert());

        converterMap.put(byte.class, new ByteConvert());
        converterMap.put(Byte.class, new ByteConvert());

        converterMap.put(short.class, new ShortConvert());
        converterMap.put(Short.class, new ShortConvert());

        converterMap.put(int.class, new IntConvert());
        converterMap.put(Integer.class, new IntConvert());

        converterMap.put(long.class, new LongConvert());
        converterMap.put(Long.class, new LongConvert());

        converterMap.put(String.class, new StringConvert());
        converterMap.put(Date.class, new DateConvert());
    }

    public static Object convert(Object val, Class clazz) throws GqSeEx {

        Converter converter = converterMap.get(clazz);

        if (converter == null) {
            return val;
        }
        Object result = converter.convert(val);

        return result;
    }
}
