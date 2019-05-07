package com.ysy.basetools.gqse.creators;

import com.ysy.basetools.gqse.GqSeContext;

/**
 * Created by guoqiang on 2017/10/20.
 */
public interface Creator<T> {
    T create(Class type, GqSeContext context);
}
