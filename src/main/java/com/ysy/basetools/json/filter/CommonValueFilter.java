package com.ysy.basetools.json.filter;

import com.alibaba.fastjson.serializer.ValueFilter;
import com.ysy.basetools.util.TimeUtil;

import java.lang.reflect.Member;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by guoqiang on 2017/7/5.
 */
public class CommonValueFilter implements ValueFilter {
    public static final CommonValueFilter ME = new CommonValueFilter();

    @Override
    public Object process(Object object, String name, Object value) {
        Object res = processObject(value);
        if (res != null) {
            return res;
        }
        return value;
    }

    public static Object processObject(Object value) {
        if (value instanceof StackTraceElement) {
            return value.toString();
        } else if (value instanceof StackTraceElement[]) {
            List<String> list = new LinkedList<>();
            for (StackTraceElement ele : ((StackTraceElement[]) value)) {
                if (ele != null) {
                    list.add(ele.toString());
                } else {
                    list.add(null);
                }
            }
            return list;
        } else if (value instanceof Date) {
            return TimeUtil.formatAllTime((Date) value);
        } else if (value instanceof Member) {
            return value.toString();
        }
        return null;
    }
}
