package com.ysy.basetools.lock;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * Created by guoqiang on 2017/9/11.
 * 多项(不可重入锁)锁实现类 K需实现equals hashcode 建议K是string
 */
public interface MultiLock<K> {
    public void lock(K k);

    public boolean tryLock(K k, long time, TimeUnit timeUnit);

    public boolean tryLock(K k);

    void unlock(K k);

    /**
     * 不建议使用
     * 容易数据造成丢失
     *
     * @param k
     * @return
     */
    public Lock getLock(K k);

    public List<K> getKeys();
}
