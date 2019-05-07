package com.ysy.basetools.map;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by guoqiang on 2016/6/22.
 */
public class CacheMap<K, V> {
    private final ConcurrentHashMap<K, Cache<V>> map;
    private long timeout = 10 * 60 * 1000;
    private long lastRemoveTime = System.currentTimeMillis();

    public void clear() {
        map.clear();
    }

    public V get(K k) {
        Cache<V> i = map.get(k);
        if (i != null && i.isValid()) {
            return i.getV();
        }
        return null;
    }

    public void removeTimeout() {
        lastRemoveTime = System.currentTimeMillis();
        try {
            Set<K> temp = map.keySet();
            for (K k : temp) {
                Cache<V> cache = map.get(k);
                if (cache != null && !cache.isValid()) {
                    map.remove(k);
                }
            }
        } catch (Exception e) {

        }
    }

    public V put(K k, V v) {
        return this.put(k, v, timeout);
    }

    public boolean refresh(K k) {
        return refresh(k, timeout);
    }

    public boolean refresh(K k, long timeout) {
        Cache<V> i = this.map.get(k);
        if (i != null) {
            i.setTime(System.currentTimeMillis() + timeout);
            return true;
        }
        return false;
    }

    public V remove(K k) {
        Cache<V> i = this.map.remove(k);
        if (i != null) {
            return i.getV();
        }
        return null;
    }

    public V put(K k, V v, long time) {
        V old = null;
        Cache<V> i = map.get(k);
        if (i != null && i.isValid()) {
            old = i.getV();
        }
        map.put(k, new Cache<V>(v, System.currentTimeMillis() + time));
        return old;
    }

    public CacheMap() {
        this.map = new ConcurrentHashMap<K, Cache<V>>();
    }

    public CacheMap(int size) {
        this.map = new ConcurrentHashMap<K, Cache<V>>(size);
    }

    public CacheMap(int size, int timeout) {
        this.map = new ConcurrentHashMap<K, Cache<V>>(size);
        this.timeout = timeout;
    }

    public long getLastRemoveTime() {
        return lastRemoveTime;
    }

    public void setLastRemoveTime(long lastRemoveTime) {
        this.lastRemoveTime = lastRemoveTime;
    }

    private static class Cache<V> {
        private long time;
        private V v;

        public Cache(V v, long endTime) {
            this.v = v;
            this.time = endTime;
        }

        public boolean isValid() {
            return System.currentTimeMillis() < time;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public V getV() {
            return v;
        }

        public void setV(V v) {
            this.v = v;
        }
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
