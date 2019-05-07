package com.ysy.basetools.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ysy.basetools.json.config.SelfSerializeConfig;
import com.ysy.basetools.log.LogUtil;
import com.ysy.basetools.util.ReflectUtil;
import com.ysy.basetools.util.TimeUtil;
import org.slf4j.Logger;

import java.util.Date;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;

/**
 * Created by guoqiang on 2015/12/9.
 */
public final class JSONUtil {
    private static final Logger logger = LogUtil.LOG;

    private static class TestModel {
        private Date date;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }

    public static void main(String ags[]) {
        TestModel model = new TestModel();
        model.setDate(new Date());
        System.out.println(TimeUtil.formatAllTime(model.getDate()));
        String str = JSONUtil.toString(Thread.currentThread().getStackTrace());
        System.out.println(str);
    }

    /**
     * 转化JSON格式
     *
     * @param text
     * @return
     */
    public static JSON parse(String text) {
        try {
            Object obj = JSON.parse(text);
            if (obj instanceof JSON) {
                return (JSON) obj;
            } else {
                return parseJSON(text);
            }
        } catch (RuntimeException e) {
            logger.error("parse fastjson error,text=" + text, e);
            throw e;
        }
    }

    public static <T> T transform(Object obj, Class<T> t) {
        try {
            return parseObject(JSON.toJSONString(obj), t);
        } catch (RuntimeException e) {
            String str = JSONWriterUtil.toJSONStr(obj);
            logger.error("警告fastjson transform error,class=" + ReflectUtil.getClass(obj)
                    + ",newJson=" + str, e);
            throw e;
        }
    }

    public static String json(Object o) {
        try {
            return toString(o);
        } catch (Exception e) {
            try {
                String str = JSONWriterUtil.toJSONStr(o);
                logger.error("警告fastjson格式化数据错误,class=" + ReflectUtil.getClass(o)
                        + ",newJson=" + str, e);
                return str;
            } catch (Exception e1) {
                logger.error("警告self json格式化数据错误", e);
                return e1.getMessage();
            }
        }
    }

    public static String toString(Object o) {
        return toString(o, UseISO8601DateFormat);
    }

    public static String toStringNative(Object o) {
        try {
            return JSON.toJSONString(o);
        } catch (RuntimeException e) {
            String str = JSONWriterUtil.toJSONStr(o);
            logger.error("警告fastjson格式化数据错误,class=" + ReflectUtil.getClass(o)
                    + ",newJson=" + str, e);
            throw e;
        }
    }

    public static String toStringC(Object o) {
        return toString(o, UseISO8601DateFormat, WriteClassName);
    }

    public static String toString(Object o, SerializerFeature... features) {
        try {
            return JSON.toJSONString(o, SelfSerializeConfig.ME, features);
        } catch (RuntimeException e) {
            String str = JSONWriterUtil.toJSONStr(o);
            logger.error("警告fastjson格式化数据错误,class=" + ReflectUtil.getClass(o)
                    + ",newJson=" + str, e);
            throw e;
        }
    }

    public static <T> T parseObject(String text, Class<T> clazz) {
        try {
            return JSON.parseObject(text, clazz);
        } catch (RuntimeException e) {
            logger.error("parseObject fastjson error,text=" + text, e);
            throw e;
        }
    }

    public static JSONObject parseJSON(String text) {
        try {
            return JSON.parseObject(text);
        } catch (RuntimeException e) {
            logger.error("parseJSON fastjson error,text=" + text, e);
            throw e;
        }
    }

    public static JSONArray parseJSONArr(String text) {
        try {
            return JSON.parseArray(text);
        } catch (RuntimeException e) {
            logger.error("parseJSONArr fastjson error,text=" + text, e);
            throw e;
        }
    }

    public static String toStringWithNull(Object o) {
        return toString(o, WriteMapNullValue, UseISO8601DateFormat);
    }

    private JSONUtil() {

    }
}
