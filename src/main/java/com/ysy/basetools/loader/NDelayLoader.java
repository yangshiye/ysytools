package com.ysy.basetools.loader;

/**
 * Created by guoqiang on 2017/2/28.
 */
public abstract class NDelayLoader<T> extends DelayLoader<T, Object> {
    @Override
    protected final T load(Object param) {
        return load();
    }

    protected abstract T load();
}
