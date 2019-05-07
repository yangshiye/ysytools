package com.ysy.basetools.json.serializer;

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
public class SelfSerializer implements ObjectSerializer {
    public static final SelfSerializer ME = new SelfSerializer();

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        if (object instanceof StackTraceElement) {
            serializer.write(object.toString());
        } else if (object instanceof Date) {
            serializer.write(TimeUtil.formatAllTime((Date) object));
        } else if (object instanceof Member) {
            serializer.write(object.toString());
        }
    }
}
