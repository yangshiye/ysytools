package com.ysy.basetools.gqse.field;

import com.ysy.basetools.util.UnsafeUtil;

import java.lang.reflect.Field;

/**
 * Created by guoqiang on 2017/10/20.
 */
public class ShortFieldOper extends FieldOper {


    public Object getValue(Object obj) {
        return UnsafeUtil.getShortValue(obj, getOffset());
    }

    @Override
    public void setDefaultValue(Object obj) {
        this.setValue(obj, (short) 0);
    }

    public void setValue(Object obj, Object value) {
        try {
            UnsafeUtil.setShortValue(obj, getOffset(), (Short) value);
        } catch (Throwable e) {
            throw new RuntimeException("设置数据错误,field=" + getFullName(), e);
        }
    }

    public ShortFieldOper(String fullName, Field field) {
        super(fullName, field);
    }
}
