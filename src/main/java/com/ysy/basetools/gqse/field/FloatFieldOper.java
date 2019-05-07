package com.ysy.basetools.gqse.field;

import com.ysy.basetools.util.UnsafeUtil;

import java.lang.reflect.Field;

/**
 * Created by guoqiang on 2017/10/20.
 */
public class FloatFieldOper extends FieldOper {


    public Object getValue(Object obj) {
        return UnsafeUtil.getFloatValue(obj, getOffset());
    }

    @Override
    public void setDefaultValue(Object obj) {
        this.setValue(obj, 0.0f);
    }

    public void setValue(Object obj, Object value) {
        try {
            UnsafeUtil.setFloatValue(obj, getOffset(), (Float) value);
        } catch (Throwable e) {
            throw new RuntimeException("设置数据错误,field=" + getFullName(), e);
        }
    }

    public FloatFieldOper(String fullName, Field field) {
        super(fullName, field);
    }
}
