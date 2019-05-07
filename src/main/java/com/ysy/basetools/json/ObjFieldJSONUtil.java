package com.ysy.basetools.json;

import com.ysy.basetools.util.ArrayUtil;
import com.ysy.basetools.util.ReflectUtil;
import com.ysy.basetools.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Administrator on 2018/7/4.
 */
public class ObjFieldJSONUtil {
    private static final Logger logger = LoggerFactory.getLogger(ObjFieldJSONUtil.class);

    public static String json(Object o) {
        if (o == null) {
            return "null";
        }
        try {
            StringBuilder build = new StringBuilder(1024);
            json(o, build);
            return build.toString();
        } catch (IllegalAccessException e) {
            Class clazz = o.getClass();
            logger.error("format data is error!class=" + clazz.getName(), e);
            return e.getMessage();
        }
    }

    private static void json(Object o, StringBuilder builder) throws IllegalAccessException {
        if (o == null) {
            builder.append("null");
            return;
        }
        Class clazz = o.getClass();
        if (o instanceof Collection) {
            builder.append("[");
            boolean flag = false;
            for (Object temp : ((Collection) o)) {
                if (!flag) {
                    flag = true;
                } else {
                    builder.append(",");
                }
                json(temp, builder);
            }
            builder.append("]");
        } else if (o instanceof Map) {
            builder.append("{");
            boolean flag = false;
            for (Map.Entry temp : ((Set<Map.Entry>) ((Map) o).entrySet())) {
                if (!flag) {
                    flag = true;
                } else {
                    builder.append(",");
                }
                json(temp.getKey(), builder);
                builder.append(":");
                json(temp.getValue(), builder);
            }
            builder.append("}");
        } else if (ArrayUtil.isArray(o)) {
            builder.append("[");
            boolean flag = false;
            int len = ArrayUtil.getSize(o);
            for (int i = 0; i < len; i++) {
                if (!flag) {
                    flag = true;
                } else {
                    builder.append(",");
                }
                Object temp = ArrayUtil.getObjByIdx(o, i);
                json(temp, builder);
            }
            builder.append("]");
        } else if (clazz.isEnum()) {
            builder.append(((Enum) o).name());
        } else if (o instanceof Date) {
            builder.append(TimeUtil.formatAllTime((Date) o));
        } else if (clazz.getClassLoader() == null) {
            builder.append(o.toString());
        } else {
            List<Field> list = ReflectUtil.getAllIFieldList(clazz);
            builder.append("{");
            boolean flag = false;
            for (Field field : list) {
                if (!flag) {
                    flag = true;
                } else {
                    builder.append(",");
                }
                field.setAccessible(true);
                Object value = field.get(o);
                builder.append(field.getName()).append(":");
                json(value, builder);
            }
            builder.append("}");
        }
    }
}
