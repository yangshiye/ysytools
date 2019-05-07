package com.ysy.basetools.interceptor.wrapper;

import com.ysy.basetools.interceptor.Interceptor;

/**
 * Created by guoqiang on 2017/6/9.
 */
public class ThreadLocalWrapper<T, R> implements AbstractWrapper<T, R> {
    private final ThreadLocal<T> threadLocal;
    private final Interceptor<T, R> interceptor;
    private final AbstractWrapper<T, R> nextWrapper;

    public ThreadLocalWrapper(ThreadLocal<T> threadLocal, Interceptor<T, R> interceptor, AbstractWrapper<T, R> wrapper) {
        this.threadLocal = threadLocal;
        this.interceptor = interceptor;
        this.nextWrapper = wrapper;
    }

    public void setT(T t) {
        this.threadLocal.set(t);
    }

    public void clearT() {
        this.threadLocal.remove();
    }

    @Override
    public R invoke() throws Exception {
        return interceptor.invoke(nextWrapper);
    }

    @Override
    public T getT() {
        return threadLocal.get();
    }
}
