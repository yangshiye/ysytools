package com.ysy.basetools.gqse.convert;

import com.ysy.basetools.gqse.exceptions.GqSeEx;

/**
 * Created by Administrator on 2018/12/7.
 */
public interface Converter<T> {
    T convert(Object obj) throws GqSeEx;
}
