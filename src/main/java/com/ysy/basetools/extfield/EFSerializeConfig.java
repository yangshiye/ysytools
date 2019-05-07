package com.ysy.basetools.extfield;

import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.ysy.basetools.json.serializer.SelfSerializer;

import java.lang.reflect.Member;
import java.util.Date;

/**
 * Created by Administrator on 2018/8/15.
 */
public class EFSerializeConfig extends SerializeConfig {

    @Override
    public ObjectSerializer getObjectWriter(Class<?> clazz) {
        if (clazz == StackTraceElement.class) {
            return SelfSerializer.ME;
        } else if (Date.class.isAssignableFrom(clazz)) {
            return SelfSerializer.ME;
        } else if (Member.class.isAssignableFrom(clazz)) {
            return SelfSerializer.ME;
        } else if (clazz.getAnnotation(EF.class) != null) {
            return EFSerializer.ME;
        }
        return super.getObjectWriter(clazz);
    }
}
