package com.ysy.basetools.gqse.field;

import com.ysy.basetools.util.UnsafeUtil;

import java.lang.reflect.Field;

/**
 * Created by guoqiang on 2017/10/20.
 */
public class CharFieldOper extends FieldOper {


    public Object getValue(Object obj) {
        return UnsafeUtil.getCharValue(obj, getOffset());
    }

    @Override
    public void setDefaultValue(Object obj) {
        this.setValue(obj, '0');
    }

    public void setValue(Object obj, Object value) {
        try {
            UnsafeUtil.setCharValue(obj, getOffset(), (Character) value);
        } catch (Throwable e) {
            throw new RuntimeException("设置数据错误,field=" + getFullName(), e);
        }
    }

    public CharFieldOper(String fullName, Field field) {
        super(fullName, field);
    }
}
