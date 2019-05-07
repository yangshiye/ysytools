package com.ysy.basetools.loader;

/**
 * Created by guoqiang on 2016/9/1.
 */
public abstract class DelayLoader<T, P> {
    private volatile T t;
    private volatile boolean init = false;

    public T get() {
        return get(null);
    }

    public T get(P p) {
        if (init) {
            return t;
        } else {
            synchronized (this) {
                if (init) {
                    return t;
                } else {
                    t = load(p);
                    init = true;
                    return t;
                }
            }
        }
    }

    protected abstract T load(P param);

    public DelayLoader() {
    }

    public DelayLoader(T t) {
        this.t = t;
    }
}
