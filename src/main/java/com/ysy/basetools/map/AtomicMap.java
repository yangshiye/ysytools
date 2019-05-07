package com.ysy.basetools.map;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by guoqiang on 2017/5/24.
 * 原子性 高并发map
 */
public class AtomicMap<K, V> implements Map<K, V> {
    private final Map<K, V>[] maps;
    private final Lock[] locks;
    private final int size;

    public AtomicMap() {
        this(32);
    }

    public AtomicMap(int size) {
        if (size < 1) {
            size = 16;
        } else if (size > 1024) {
            size = 1024;
        }
        this.size = size;
        this.maps = new Map[size];
        this.locks = new Lock[size];
        for (int i = 0; i < size; i++) {
            this.maps[i] = new HashMap<K, V>(32);
            this.locks[i] = new ReentrantLock();
        }
    }

    @Override
    public int size() {
        int size = 0;
        for (Map map : maps) {
            size += map.size();
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        for (Map map : maps) {
            if (!map.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean containsKey(Object key) {
        int i = getIdxByKey(key);
        return maps[i].containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        for (Map map : maps) {
            if (map.containsValue(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 如果key对应的value不存在则put值
     *
     * @param key
     * @param value
     * @return
     */
    public PutRes<V> putIfNEX(K key, V value) {
        int i = getIdxByKey(key);
        lockByIdx(i);
        try {
            Map<K, V> map = maps[i];
            if (map.containsKey(key)) {
                V temp = map.get(key);
                return new PutRes<V>(temp, temp, false);
            } else {
                V temp = map.put(key, value);
                return new PutRes<V>(value, temp, true);
            }
        } finally {
            unLockByIdx(i);
        }
    }

    /**
     * 如果key对应的value不存在或者跟oldValue一致则put值
     *
     * @param key
     * @param newValue
     * @param oldValue
     * @return
     */
    public PutRes<V> putIfNEXOrNEQ(K key, V newValue, V oldValue) {
        int i = getIdxByKey(key);
        lockByIdx(i);
        try {
            Map<K, V> map = maps[i];
            if (map.containsKey(key)) {
                V temp = map.get(key);
                if (eqValue(temp, oldValue)) {
                    temp = map.put(key, newValue);
                    return new PutRes<V>(newValue, temp, true);
                } else {
                    return new PutRes<V>(temp, temp, false);
                }
            } else {
                V temp = map.put(key, newValue);
                return new PutRes<V>(newValue, temp, true);
            }
        } finally {
            unLockByIdx(i);
        }
    }

    private boolean eqValue(V v1, V v2) {
        if (v1 == v2) {
            return true;
        } else if (v1 != null) {
            return v1.equals(v2);
        }
        return false;
    }

    @Override
    public V get(Object key) {
        int i = getIdxByKey(key);
        lockByIdx(i);
        try {
            return maps[i].get(key);
        } finally {
            unLockByIdx(i);
        }
    }

    @Override
    public V put(K key, V value) {
        int i = getIdxByKey(key);
        lockByIdx(i);
        try {
            return maps[i].put(key, value);
        } finally {
            unLockByIdx(i);
        }
    }

    @Override
    public V remove(Object key) {
        int i = getIdxByKey(key);
        lockByIdx(i);
        try {
            return maps[i].remove(key);
        } finally {
            unLockByIdx(i);
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            lockByIdx(i);
            try {
                maps[i].clear();
            } finally {
                unLockByIdx(i);
            }
        }
    }

    private int getIdxByKey(Object key) {
        if (key == null) {
            return 0;
        }
        int h = (h = key.hashCode()) ^ (h >>> 16);
        return Math.abs(h % this.size);
    }

    private void unLockByIdx(int i) {
        locks[i].unlock();
    }

    private void lockByIdx(int i) {
        locks[i].lock();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if (m != null) {
            for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
                this.put(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<K>();
        for (int i = 0; i < size; i++) {
            lockByIdx(i);
            try {
                set.addAll(maps[i].keySet());
            } finally {
                unLockByIdx(i);
            }
        }
        return set;
    }

    @Override
    public Collection<V> values() {
        List<V> list = new LinkedList<V>();
        for (int i = 0; i < size; i++) {
            lockByIdx(i);
            try {
                list.addAll(maps[i].values());
            } finally {
                unLockByIdx(i);
            }
        }
        return list;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> set = new HashSet<Entry<K, V>>();
        for (int i = 0; i < size; i++) {
            lockByIdx(i);
            try {
                set.addAll(maps[i].entrySet());
            } finally {
                unLockByIdx(i);
            }
        }
        return set;
    }

    public static class PutRes<V> {
        private final V newValue;
        private final V oldValue;
        private final boolean setSuc;

        public V getNewValue() {
            return newValue;
        }

        public V getOldValue() {
            return oldValue;
        }

        public boolean isSetSuc() {
            return setSuc;
        }

        public PutRes(V newValue, V oldValue, boolean setSuc) {
            this.newValue = newValue;
            this.oldValue = oldValue;
            this.setSuc = setSuc;
        }
    }
}
