package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

import java.util.Date;

/**
 * Created by guoqiang on 2017/10/25.
 */
public class DateSerializerFactory extends SerializerFactory<Date> {

    @Override
    public void doWriteObj(Date obj, Output output, GqSeContext context, int depth) {
        output.writeLong(obj.getTime());
    }

    @Override
    public Date readObj(Input input, GqSeContext context) {
        return new Date(input.readLong());
    }
}
