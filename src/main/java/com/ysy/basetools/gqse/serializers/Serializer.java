package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

/**
 * Created by guoqiang on 2017/10/19.
 */
public interface Serializer<T> {
    void writeObj(T obj, Output output, GqSeContext context, int depth);

    <V> V readObj(Input input, GqSeContext context);
}
