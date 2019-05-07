package com.ysy.basetools.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by guoqiang on 2017/9/11.
 *
 */
public class MultiLockImpl<K> implements MultiLock<K> {
    private final ConcurrentMap<K, ReentrantLock> lockMap;

    @Override
    public void lock(K o) {
        ReentrantLock newLock = new ReentrantLock();
        newLock.lock();
        ReentrantLock oldLock;
        do {
            oldLock = lockMap.putIfAbsent(o, newLock);
            if (oldLock != null) {
                oldLock.lock();
                oldLock.unlock();
            }
        } while (oldLock != null);
    }

    @Override
    public void unlock(K o) {
        ReentrantLock lock = lockMap.remove(o);
        if (lock != null) {
            lock.unlock();
        }
    }

    @Override
    public boolean tryLock(K o, long time, TimeUnit timeUnit) {
        long endTime = System.currentTimeMillis() + timeUnit.toMillis(time);
        ReentrantLock newLock = new ReentrantLock();
        newLock.lock();
        do {
            ReentrantLock oldLock = lockMap.putIfAbsent(o, newLock);
            if (oldLock != null) {
                try {
                    long lockTime = endTime - System.currentTimeMillis();
                    if (lockTime > 0) {
                        if (oldLock.tryLock(lockTime, TimeUnit.MILLISECONDS)) {
                            oldLock.unlock();
                        }
                    } else {
                        return false;
                    }
                } catch (InterruptedException e) {
                }
            } else {
                return true;
            }
        } while (System.currentTimeMillis() < endTime);

        return false;
    }

    @Override
    public boolean tryLock(K o) {
        ReentrantLock newLock = new ReentrantLock();
        newLock.lock();
        ReentrantLock oldLock = lockMap.putIfAbsent(o, newLock);

        return oldLock == null;
    }

    @Override
    public Lock getLock(K o) {
        return lockMap.get(o);
    }

    @Override
    public List<K> getKeys() {
        return new ArrayList<K>(lockMap.keySet());
    }

    public MultiLockImpl() {
        this(128);
    }

    public MultiLockImpl(int size) {
        this.lockMap = new ConcurrentHashMap<K, ReentrantLock>(size);
    }
}
