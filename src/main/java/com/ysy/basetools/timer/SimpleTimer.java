package com.ysy.basetools.timer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by guoqiang on 2017/9/18.
 */
@Deprecated
public class SimpleTimer {
    private final Lock lock = new ReentrantLock();
    private long time;
    private long timeout;

    public boolean isNow() {
        if (lock.tryLock()) {
            try {
                long nowTime = System.currentTimeMillis();
                if (nowTime - time > timeout) {
                    time = nowTime;

                    return true;
                } else {
                    return false;
                }
            } finally {
                lock.unlock();
            }
        } else {
            return false;
        }
    }

    public SimpleTimer(long timeout) {
        this.timeout = timeout;
    }

    public SimpleTimer(long timeout, TimeUnit timeUnit) {
        this.timeout = timeUnit.toMillis(timeout);
    }

    public SimpleTimer() {
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
