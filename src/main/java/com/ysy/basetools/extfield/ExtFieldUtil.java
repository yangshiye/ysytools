package com.ysy.basetools.extfield;

import com.ysy.basetools.enums.EnumUtil;
import com.ysy.basetools.enums.IEnum;
import com.ysy.basetools.extfield.impl.DateExtField;
import com.ysy.basetools.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Administrator on 2018/5/8.
 */
public class ExtFieldUtil {
    private static DateExtField dateExtField = new DateExtField();

    public static List<Map> genConvertList(List list) {
        if (list != null) {
            List<Map> result = new ArrayList(list.size());
            for (Object obj : list) {
                Map newObj = convert(obj);
                result.add(newObj);
            }

            return result;
        }
        return null;
    }

    public static void convertList(List list) {
        if (list != null) {
            for (ListIterator it = list.listIterator(); it.hasNext(); ) {
                Object obj = it.next();
                Object newObj = convert(obj);
                it.set(newObj);
            }
        }
    }

    public static Map<String, Object> convert(Object obj) {
        if (obj == null) {
            return null;
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<Field> list = ReflectUtil.getAllIFieldList(obj.getClass());
        for (Field field : list) {
            EF ef = field.getAnnotation(EF.class);
            EFS efs = field.getAnnotation(EFS.class);
            String fieldName = field.getName();
            Object oldValue = ReflectUtil.getFieldValue(obj, field);
            if (efs != null && !efs.ignore()) {
                EF[] efArr = efs.efs();
                if (efArr != null) {
                    for (EF temp : efArr) {
                        if (temp != null && !temp.ignore()) {
                            putValByEF(temp, obj, fieldName, oldValue, map);
                        }
                    }
                }
            } else if (ef != null && !ef.ignore()) {
                putValByEF(ef, obj, fieldName, oldValue, map);
            } else {
                map.put(fieldName, oldValue);
            }
        }
        return map;
    }

    private static void putValByEF(EF ef, Object obj, String fieldName, Object oldValue, Map<String, Object> map) {
        Class clazz = ef.extField();
        Object newValue = null;
        if (IEnum.class.isAssignableFrom(clazz)) {
            newValue = EnumUtil.getName(clazz, oldValue);
        } else if (ExtField.class.isAssignableFrom(clazz) && clazz != ExtField.class) {
            if (clazz == DateExtField.class) {
                newValue = dateExtField.convert((Date) oldValue, obj, ef);
            } else {
                ExtField extField = (ExtField) ReflectUtil.newInstance(clazz);
                newValue = extField.convert(oldValue, obj, ef);
            }
        } else if (oldValue instanceof Date) {
            newValue = dateExtField.convert((Date) oldValue, obj, ef);
        }
        if (ef.override()) {
            map.put(fieldName, newValue);
        } else {
            map.put(fieldName, oldValue);
            map.put(ef.name(), newValue);
        }
    }
}
