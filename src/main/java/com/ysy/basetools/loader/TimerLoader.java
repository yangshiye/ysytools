package com.ysy.basetools.loader;

import com.ysy.basetools.log.LogUtil;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2017/1/30.
 */
public abstract class TimerLoader<Param, Result> {
    private static final Logger logger = LogUtil.LOG;
    private static volatile long flagTime = System.currentTimeMillis();
    private long timeout = 30 * 1000;
    private volatile Long lastReadTime = null;
    private volatile Result v;
    private final Lock lock = new ReentrantLock();

    public abstract Result doGetRealValue(Param k);

    public Result getByTime() {
        return getByTime(null);
    }

    public Result getByTime(Param k) {
        Result result;
        long time = System.currentTimeMillis() - timeout;

        if (needRead(time)) {

            //写的这么复杂是因为解决findbugs的问题
            if (lastReadTime == null) {
                lock.lock();
            } else {
                boolean lockResult = lock.tryLock();
                if (!lockResult) {
                    result = getV();
                    return result;
                }
            }
            try {
                if (needRead(time)) {
                    result = flush(k);
                } else {
                    result = getV();
                }
            } catch (Exception e) {
                logger.error("this = " + this + ", key = " + k, e);
                result = getV();
            } finally {
                lock.unlock();
            }
        } else {
            result = getV();
        }

        return result;
    }

    private boolean needRead(long time) {
        return lastReadTime == null || (lastReadTime < time && timeout >= 0) || lastReadTime < flagTime;
    }

    public Result flush(Param k) {
        Result result = doGetRealValue(k);
        setV(result);
        this.lastReadTime = System.currentTimeMillis();
        return result;
    }

    public TimerLoader(long time) {
        this.timeout = time;
    }

    public TimerLoader(long time, TimeUnit timeUnit) {
        this.timeout = timeUnit.toMillis(time);
    }

    public Result getV() {
        return v;
    }

    public void setV(Result v) {
        this.v = v;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public Long getLastReadTime() {
        return lastReadTime;
    }

    public void setLastReadTime(Long lastReadTime) {
        this.lastReadTime = lastReadTime;
    }

    public static long resetFlagTime() {
        long time = System.currentTimeMillis();
        flagTime = time;
        return time;
    }

    public static long getFlagTime() {
        return flagTime;
    }
}