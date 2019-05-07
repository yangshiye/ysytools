package com.ysy.basetools.lock;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by guoqiang on 2017/9/11.
 */
public class SimpleLock {
    private final AtomicBoolean value = new AtomicBoolean(false);

    public boolean tryLock() {
        return value.compareAndSet(false, true);
    }

    public void unlock() {
        value.compareAndSet(true, false);
    }
}
