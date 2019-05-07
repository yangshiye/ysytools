package com.ysy.basetools.interceptor.wrapper;

/**
 * Created by guoqiang on 2017/6/9.
 */
public interface AbstractWrapper<T, R> {
    R invoke() throws Exception;

    T getT();
}
