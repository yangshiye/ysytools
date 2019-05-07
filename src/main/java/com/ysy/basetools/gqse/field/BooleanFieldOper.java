package com.ysy.basetools.gqse.field;

import com.ysy.basetools.util.UnsafeUtil;

import java.lang.reflect.Field;

/**
 * Created by guoqiang on 2017/10/20.
 */
public class BooleanFieldOper extends FieldOper {

    @Override
    public Object getValue(Object obj) {
        return UnsafeUtil.getBooleanValue(obj, getOffset());
    }


    @Override
    public void setDefaultValue(Object obj) {
        this.setValue(obj, false);
    }

    @Override
    public void setValue(Object obj, Object value) {
        try {
            UnsafeUtil.setBoolValue(obj, getOffset(), (Boolean) value);
        } catch (Throwable e) {
            throw new RuntimeException("设置数据错误,field=" + getFullName(), e);
        }
    }

    public BooleanFieldOper(String fullName, Field field) {
        super(fullName, field);
    }
}
