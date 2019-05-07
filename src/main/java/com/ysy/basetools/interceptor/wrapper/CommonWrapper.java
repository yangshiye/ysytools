package com.ysy.basetools.interceptor.wrapper;

import com.ysy.basetools.interceptor.Interceptor;

/**
 * Created by guoqiang on 2017/6/9.
 */
public class CommonWrapper<T, R> implements AbstractWrapper<T, R> {
    private final AbstractWrapper<T, R> nextWrapper;
    private final Interceptor<T, R> interceptor;
    private final T t;

    public CommonWrapper(T t, Interceptor<T, R> interceptor, AbstractWrapper<T, R> nextWrapper) {
        this.t = t;
        this.nextWrapper = nextWrapper;
        this.interceptor = interceptor;
    }

    @Override
    public R invoke() throws Exception {
        R r = interceptor.invoke(nextWrapper);
        return r;
    }

    @Override
    public T getT() {
        return t;
    }
}
