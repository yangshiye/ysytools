package com.ysy.basetools.gqse.field;

import java.lang.reflect.Field;

/**
 * Created by guoqiang on 2017/10/20.
 */
public class ObjFieldOper extends FieldOper {


    @Override
    public void setDefaultValue(Object obj) {
        this.setValue(obj, null);
    }

    public ObjFieldOper(String fullName, Field field) {
        super(fullName, field);
    }
}
