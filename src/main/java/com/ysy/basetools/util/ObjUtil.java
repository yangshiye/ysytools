package com.ysy.basetools.util;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Administrator on 2018/5/10.
 */
public final class ObjUtil {

    public static <T> T getFiledValue(Object obj, String name) {
        if (obj == null || name == null) {
            return null;
        } else if (obj instanceof Map) {
            return (T) ((Map) obj).get(name);
        } else {
            return (T) ReflectUtil.getFieldValue(obj, name);
        }
    }

    public static <T> T objToDS(Object object) {
        if (object == null) {
            return null;
        }
        Object result = null;
        if (object instanceof Collection) {
            List list = new ArrayList(((Collection) object).size());
            for (Object obj : (Collection) object) {
                list.add(objToDS(obj));
            }
            return (T) list;
        } else if (object instanceof Map) {
            Map map = new HashMap(((Map) object).size());
            for (Map.Entry entry : ((Map<?, ?>) object).entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                map.put(objToDS(key), objToDS(value));
            }
            return (T) map;
        } else {
            Class clazz = object.getClass();
            if (clazz.isArray()) {
                List list = new LinkedList();
                if (object instanceof int[]) {
                    for (int i : (int[]) object) {
                        list.add(i);
                    }
                } else if (object instanceof long[]) {
                    for (long i : (long[]) object) {
                        list.add(i);
                    }
                } else if (object instanceof byte[]) {
                    for (byte i : (byte[]) object) {
                        list.add(i);
                    }
                } else if (object instanceof short[]) {
                    for (short i : (short[]) object) {
                        list.add(i);
                    }
                } else if (object instanceof boolean[]) {
                    for (boolean i : (boolean[]) object) {
                        list.add(i);
                    }
                } else if (object instanceof char[]) {
                    for (char i : (char[]) object) {
                        list.add(i);
                    }
                } else if (object instanceof float[]) {
                    for (float i : (float[]) object) {
                        list.add(i);
                    }
                } else if (object instanceof double[]) {
                    for (double i : (double[]) object) {
                        list.add(i);
                    }
                } else {
                    Object arr[] = (Object[]) object;
                    for (Object temp : arr) {
                        list.add(objToDS(temp));
                    }
                }
                return (T) list;
            } else if (clazz.isEnum()) {//枚举写名字
                return (T) ((Enum) object).name();
            } else if (clazz.getClassLoader() == null) {//java原生类
                return (T) object;
            } else {
                List<Field> fileds = ReflectUtil.getAllIFieldList(clazz);
                Map map = new HashMap(fileds.size());
                for (Field field : fileds) {
                    Object value = ReflectUtil.getFieldValue(object, field);
                    map.put(field.getName(), objToDS(value));
                }
                return (T) map;
            }
        }
    }

    public static <Base, Sub extends Base> List<Sub> convertBSList(List<Base> list, Class<Sub> subClass) {
        if (list == null) {
            return null;
        } else {
            List<Sub> result = ListUtil.createSameSize(list);
            for (Base b : list) {
                result.add(convertBS(b, subClass));
            }
            return result;
        }
    }

    public static <Base, Sub extends Base> Sub convertBS(Base base, Class<Sub> subClass) {
        Sub subObj = ReflectUtil.newInstance(subClass);
        return convertBS(base, subObj);
    }

    public static <Base, Sub extends Base> Sub convertBS(Base base, Sub sub) {
        Class<Base> baseClass = (Class<Base>) base.getClass();
        for (Field field : ReflectUtil.getAllIFieldList(baseClass)) {
            Object value = ReflectUtil.getFieldValue(base, field);
            ReflectUtil.setFieldValue(sub, field, value);
        }
        return sub;
    }


    private ObjUtil() {

    }
}
