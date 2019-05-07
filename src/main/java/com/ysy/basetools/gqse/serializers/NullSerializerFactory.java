package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

/**
 * Created by guoqiang on 2017/10/21.
 */
public class NullSerializerFactory extends SerializerFactory {
    @Override
    public void doWriteObj(Object obj, Output output, GqSeContext context, int depth) {

    }

    @Override
    public Object readObj(Input input, GqSeContext context) {
        return null;
    }
}
