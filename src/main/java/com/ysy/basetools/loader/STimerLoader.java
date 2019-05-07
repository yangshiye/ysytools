package com.ysy.basetools.loader;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2018/7/28.
 */
public abstract class STimerLoader<P, V> {
    private final long timeout;//单位纳秒
    private final long preTimeout;//预超时时间 小于timeout单位纳秒
    private final Lock lock = new ReentrantLock();
    private final AtomicBoolean asyncLoadFlag = new AtomicBoolean(false);
    private volatile Long lastLoadTime;//单位纳秒
    private volatile V v;

    //如果需要异步加载可以重写
    //new Thread(new Runnable() {
    //    @Override
    //    public void run() {
    //        loader.load();
    //    }
    //}).start();
    public void noticeToCouldLoad(SLoader<P, V> loader) {
    }

    /**
     * 真实加载数据 需要重写
     *
     * @param p
     * @return
     */
    public abstract V doLoad(P p);

    public V get() {
        return get(null);
    }

    public V get(P p) {
        long time = System.nanoTime();
        if (isMustRun(time)) {
            return toForceLoad(p, time);
        } else if (preTimeout > 0 && !asyncLoadFlag.get() && isCouldRun(time)) {
            if (asyncLoadFlag.compareAndSet(false, true)) {
                noticeToCouldLoad(new SLoader<>(p, this));
            }
        }
        return v;
    }

    /**
     * 强制加载
     *
     * @param p
     * @param time
     * @return
     */
    private V toForceLoad(P p, long time) {
        lock.lock();
        try {
            if (isMustRun(time)) {
                v = doLoad(p);
                lastLoadTime = System.nanoTime();
                asyncLoadFlag.set(false);
            }
        } finally {
            lock.unlock();
        }
        return v;
    }

    private V asyncLoad(P p, long time) {
        if (lock.tryLock()) {
            try {
                if (isCouldRun(time)) {
                    v = doLoad(p);
                    lastLoadTime = System.nanoTime();
                    asyncLoadFlag.set(false);
                }
            } finally {
                lock.unlock();
            }
        }
        return v;
    }

    private boolean isCouldRun(long time) {
        if (lastLoadTime == null) {
            return true;
        } else if (lastLoadTime + preTimeout < time) {
            return true;
        }
        return false;
    }

    private boolean isMustRun(long time) {
        if (lastLoadTime == null) {
            return true;
        } else if (lastLoadTime + timeout < time) {
            return true;
        }
        return false;
    }

    protected STimerLoader(long timeout, long preTimeout) {
        this.timeout = TimeUnit.MILLISECONDS.toNanos(timeout);
        this.preTimeout = TimeUnit.MILLISECONDS.toNanos(preTimeout);
    }

    public static class SLoader<P, V> {
        private final P param;
        private final STimerLoader<P, V> sTimerLoader;

        public SLoader(P p, STimerLoader<P, V> pvsTimerLoader) {
            param = p;
            sTimerLoader = pvsTimerLoader;
        }

        public V load() {
            return sTimerLoader.asyncLoad(param, System.nanoTime());
        }
    }

}
