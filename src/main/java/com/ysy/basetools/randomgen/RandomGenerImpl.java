package com.ysy.basetools.randomgen;

import com.ysy.basetools.log.LogUtil;
import com.ysy.basetools.util.ListUtil;
import com.ysy.basetools.util.StrUtil;
import com.ysy.basetools.util.TimeUtil;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by guoqiang on 2016/7/25.
 */
public class RandomGenerImpl implements RandomGener {
    private static final Logger logger = LogUtil.LOG;
    private final Random r = new Random();
    private final Map<String, List<Object>> valueMap;

    @Override
    public <T> T getValue(Class<T> currentClass) {
        return getValue(currentClass, null, null, "");
    }

    private <T> T getValue(Class<T> currentClass, Class parentClass, Field field, String path) {
        Object t;
        if (currentClass == null) {
            return null;
        }
        if (valueMap != null) {
            List<Object> list = valueMap.get(path);
            if (ListUtil.isNotEmpty(list)) {
                Object obj = list.get(r.nextInt(list.size()));
                return (T) obj;
            }
        }
        if (currentClass == String.class) {
            if (field != null) {
                t = field.getName() + "_" + UUID.randomUUID().toString().substring(0, 4);
            } else {
                t = "STRING_" + UUID.randomUUID().toString().substring(0, 4);
            }
        } else if (currentClass == int.class || currentClass == Integer.class) {
            t = r.nextInt(100);
        } else if (currentClass == byte.class || currentClass == Byte.class) {
            t = (byte) r.nextInt(10);
        } else if (currentClass == short.class || currentClass == Short.class) {
            t = (short) r.nextInt(100);
        } else if (currentClass == long.class || currentClass == Long.class) {
            t = (long) r.nextInt(100);
        } else if (currentClass == boolean.class || currentClass == Boolean.class) {
            t = (r.nextInt(2) == 0);
        } else if (currentClass == float.class || currentClass == Float.class) {
            t = (((float) r.nextInt(10000)) / 100.0f);
        } else if (currentClass == double.class || currentClass == Double.class) {
            t = (((double) r.nextInt(10000)) / 100.0);
        } else if (currentClass == char.class || currentClass == Character.class) {
            t = 'A' + r.nextInt(26);
        } else if (currentClass == BigDecimal.class) {
            t = new BigDecimal((double) r.nextInt(100));
        } else if (currentClass == BigInteger.class) {
            t = new BigInteger(String.valueOf(r.nextInt(100)), 10);
        } else if (currentClass == Date.class) {
            Date d = TimeUtil.createNowTime();
            TimeUtil.set0H0M0S0MS(d);
            d.setTime(d.getTime() + (r.nextInt(90)) * 15 * 60 * 1000);
            t = d;
        } else if (currentClass == java.sql.Date.class) {
            Date d = TimeUtil.createNowTime();
            TimeUtil.set0H0M0S0MS(d);
            d.setTime(d.getTime() + (r.nextInt(90)) * 15 * 60 * 1000);
            t = new java.sql.Date(d.getTime());
        } else if (currentClass == Timestamp.class) {
            Date d = TimeUtil.createNowTime();
            TimeUtil.set0H0M0S0MS(d);
            d.setTime(d.getTime() + (r.nextInt(90)) * 15 * 60 * 1000);
            t = new Timestamp(d.getTime());
        } else if (currentClass.isAssignableFrom(ArrayList.class)) {
            ArrayList list = new ArrayList(2);
            if (field != null && field.getGenericType() != null
                    && field.getGenericType() instanceof ParameterizedType) {
                for (int i = 0; i < 2; i++) {
                    Object obj = getValue((Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0], parentClass, null, path);
                    list.add(obj);
                }
            } else if (field == null) {
                for (int i = 0; i < 2; i++) {
                    list.add(r.nextInt(100));
                }
            }
            return (T) list;
        } else if (Map.class.isAssignableFrom(currentClass)) {
            logger.error("currentClass = " + currentClass + ",path = " + path + " is can not gen object");
            return null;//map结构的无法正常生成数据
        } else {
            try {
                t = currentClass.newInstance();
            } catch (Exception e) {
                logger.error("currentClass = " + currentClass + ",path = " + path, e);
                return null;
            }
            if (t != null) {
                for (Field child : currentClass.getDeclaredFields()) {
                    if (!Modifier.isFinal(child.getModifiers()) && !Modifier.isStatic(child.getModifiers())) {
                        String newPath;
                        if (StrUtil.isEmpty(path)) {
                            newPath = child.getName();
                        } else {
                            newPath = path + "." + child.getName();
                        }
                        Object obj = getValue(child.getType(), currentClass, child, newPath);
                        child.setAccessible(true);
                        try {
                            child.set(t, obj);
                        } catch (IllegalAccessException e) {
                            logger.error("currentClass = " + currentClass + ",path = " + path
                                    + ", child = " + child, e);
                        }
                    }
                }
            }
        }
        return (T) t;
    }

    public RandomGenerImpl() {
        valueMap = null;
    }

    public RandomGenerImpl(Map<String, List<Object>> valueMap) {
        this.valueMap = valueMap;
    }
}
