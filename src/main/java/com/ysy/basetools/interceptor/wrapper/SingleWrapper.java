package com.ysy.basetools.interceptor.wrapper;

import com.ysy.basetools.interceptor.Interceptor;

import java.util.Iterator;
import java.util.List;

/**
 * Created by guoqiang on 2017/6/9.
 */
public class SingleWrapper<T, R> implements AbstractWrapper<T, R> {
    private final List<? extends Interceptor<T, R>> interceptors;
    private final T t;
    private final Iterator<? extends Interceptor<T, R>> it;

    public SingleWrapper(T t, List<? extends Interceptor<T, R>> interceptors) {
        this.t = t;
        this.interceptors = interceptors;
        this.it = interceptors.iterator();
    }

    @Override
    public R invoke() throws Exception {
        Interceptor<T, R> interceptor = it.next();
        return interceptor.invoke(this);
    }

    @Override
    public T getT() {
        return t;
    }
}