package com.ysy.basetools.gqse.field;

import com.ysy.basetools.util.UnsafeUtil;

import java.lang.reflect.Field;

/**
 * Created by guoqiang on 2017/10/20.
 */
public abstract class FieldOper {
    private final String fullName;
    private final Field field;
    private final long offset;

    public FieldOper(String fullName, Field field) {
        this.fullName = fullName;
        this.field = field;
        this.offset = UnsafeUtil.getOffset(field);
    }

    public abstract void setDefaultValue(Object obj);

    public void setValue(Object obj, Object value) {
        try {
            UnsafeUtil.setValue(obj, getOffset(), value);
        } catch (Throwable e) {
            throw new RuntimeException("设置数据错误,field=" + fullName, e);
        }
    }

    public Object getValue(Object obj) {
        try {
            return UnsafeUtil.getValue(obj, getOffset());
        } catch (Throwable e) {
            throw new RuntimeException("获取数据错误,field=" + fullName, e);
        }
    }

    public Field getField() {
        return field;
    }


    protected long getOffset() {
        return offset;
    }

    public String getFullName() {
        return fullName;
    }
}
