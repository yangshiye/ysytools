package com.ysy.basetools.interceptor;

import com.ysy.basetools.interceptor.wrapper.AbstractWrapper;

/**
 * Created by guoqiang on 2017/6/9.
 */
public interface Interceptor<T, R> {
    R invoke(AbstractWrapper<T, R> wrapper) throws Exception;
}
