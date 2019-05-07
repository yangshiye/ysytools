package com.ysy.basetools.extfield;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.ysy.basetools.util.TimeUtil;

import java.io.IOException;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by Administrator on 2018/8/15.
 */
public class EFSerializer implements ObjectSerializer {

    public static final EFSerializer ME = new EFSerializer();

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        if (object instanceof StackTraceElement) {
            serializer.write(object.toString());
        } else if (object instanceof Date) {
            serializer.write(TimeUtil.formatAllTime((Date) object));
        } else if (object instanceof Member) {
            serializer.write(object.toString());
        } else if (object.getClass().getAnnotation(EF.class) != null) {
            serializer.write(ExtFieldUtil.convert(object));
        }
    }
}
