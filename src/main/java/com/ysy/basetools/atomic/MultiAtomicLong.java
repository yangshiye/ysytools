package com.ysy.basetools.atomic;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by guoqiang on 2017/5/24.
 * 多个数据的原子操作类 具体用法可以参考AtomicLong
 */
public class MultiAtomicLong<K> {
    private final Map<K, AtomicLong> map = new HashMap<K, AtomicLong>();

    public synchronized Map<K, Long> get() {
        Map<K, Long> result = new HashMap<K, Long>();
        for (K key : map.keySet()) {
            long value = 0;
            AtomicLong atomicLong = map.get(key);
            if (atomicLong != null) {
                value = atomicLong.get();
            }
            result.put(key, value);
        }
        return result;
    }

    public synchronized void set(VModel<K>... models) {
        if (models != null) {
            for (VModel<K> model : models) {
                AtomicLong value = map.get(model.key);
                if (value == null) {
                    value = new AtomicLong();
                    map.put(model.key, value);
                }
                value.set(model.value);
            }
        }
    }

    public synchronized Map<K, Long> addAndGet(VModel<K>... models) {
        if (models != null) {
            for (VModel<K> model : models) {
                AtomicLong value = map.get(model.key);
                if (value == null) {
                    value = new AtomicLong();
                    map.put(model.key, value);
                }
                value.addAndGet(model.value);
            }
        }
        return get();
    }

    public synchronized Map<K, Long> getAndSet(VModel<K>... models) {
        Map<K, Long> result = get();
        if (models != null) {
            set(models);
            for (VModel<K> model : models) {
                if (!result.containsKey(model.key)) {
                    result.put(model.key, 0L);
                }
            }
        }
        return result;
    }

    public MultiAtomicLong() {
    }

    public MultiAtomicLong(VModel<K>... models) {
        this.set(models);
    }

    public static class VModel<K> {
        private final K key;
        private final long value;

        public VModel(K key, long value) {
            this.key = key;
            this.value = value;
        }
    }
}
