package com.ysy.basetools.job.impl;

import com.ysy.basetools.job.Job;
import com.ysy.basetools.job.JobResult;
import com.ysy.basetools.lock.SimpleLock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by guoqiang on 2017/6/29.
 */
public class TimesLastJob<T, P> {
    private static final Object INVALID_OBJ = new Object();
    private final SimpleLock lock = new SimpleLock();
    private final Job<T, P> job;
    private final AtomicReference<Object> hasDoJob = new AtomicReference<Object>(INVALID_OBJ);
    private final long timeout;

    public JobResult<T, P> doJob(P p) {
        T t;
        hasDoJob.set(p);//设置当前job对象需要继续执行
        Object realParam;
        boolean flag = true;
        long time = System.currentTimeMillis();
        do {
            if (lock.tryLock()) {//加锁 防止并发执行
                try {

                    realParam = hasDoJob.getAndSet(INVALID_OBJ);//获取job是否需要执行 并且设置job无需再执行

                    if (realParam == INVALID_OBJ) {//如果无需执行则返回结果
                        return JobResult.err(p);
                    }

                    t = job.doJob((P) realParam);//执行Job
                } finally {
                    lock.unlock();//解锁
                }

            } else {//加锁失败 代表当前有线程在执行Job

                return JobResult.err(p);//返回失败

            }
            if (timeout > -1) {
                if (System.currentTimeMillis() - time > timeout) {
                    flag = false;
                }
            }
        } while (hasDoJob.get() != INVALID_OBJ && flag);//判断Job是否还需执行

        return JobResult.suc(t, (P) realParam);
    }

    public TimesLastJob(Job<T, P> job) {
        this.job = job;
        this.timeout = -1;
    }

    public TimesLastJob(Job<T, P> job, long timeout) {
        this.job = job;
        this.timeout = timeout;
    }
}
