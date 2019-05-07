package com.ysy.basetools.lrucache;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by guoqiang on 2017/6/18.
 */
public class LRULMap<K, V> extends LRUMap<K, V> {
    private final Loader<K, V> loader;
    private static final AtomicLong wholeGetCount = new AtomicLong(0);//全局查询次数
    private static final AtomicLong wholeLoadCount = new AtomicLong(0);//全局加载次数

    public static <K, V> LRULMap<K, V> create(Loader<K, V> loader, int size, long time, TimeUnit unit) {
        return new LRULMap<K, V>(loader, size, time, unit);
    }

    public static <K, V> LRULMap<K, V> createHours(Loader<K, V> loader, int size, int time) {
        return new LRULMap<K, V>(loader, size, time, TimeUnit.HOURS);
    }

    public static <K, V> LRULMap<K, V> createMinutes(Loader<K, V> loader, int size, int time) {
        return new LRULMap<K, V>(loader, size, time, TimeUnit.MINUTES);
    }

    public LRULMap(Loader<K, V> loader, int size) {
        super(size);
        if (loader == null) {
            throw new NullPointerException("loader can not be null");
        }
        this.loader = loader;
    }

    public LRULMap(Loader<K, V> loader, int size, long time, TimeUnit unit) {
        super(size, time, unit);
        if (loader == null) {
            throw new NullPointerException("loader can not be null");
        }
        this.loader = loader;
    }

    public V fLoad(K k) {
        V v = loader.load(k);
        this.put(k, v);
        return v;
    }

    /**
     * 仅供参考 禁止使用
     *
     * @return
     */
    @Deprecated
    public static long getWholeGetCount() {
        return wholeGetCount.get();
    }

    /**
     * 仅供参考 禁止使用
     *
     * @return
     */
    @Deprecated
    public static long getWholeLoadCount() {
        return wholeLoadCount.get();
    }

    @Override
    public V get(Object key) {
        wholeGetCount.incrementAndGet();
        K k = (K) key;
        LRUMap.LRUModel<K, V> model = this.doGet(key);
        if (model == null || model.invalid()) {
            wholeLoadCount.incrementAndGet();
            return fLoad(k);
        } else {
            return model.getV();
        }
    }

    public V onlyGet(Object key) {
        return super.get(key);
    }
}
