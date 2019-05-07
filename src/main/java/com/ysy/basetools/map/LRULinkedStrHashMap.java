package com.ysy.basetools.map;


import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.Weigher;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class LRULinkedStrHashMap<K> implements Map<K, String> {
    private static final int DEFAULT_CONCURRENCY_LEVEL = 64;//最大并发数
    private static final int CAPACITY = 10240000;//占用多少字节内存

    private final ConcurrentLinkedHashMap<K, String> map;
    private final AtomicLong requests = new AtomicLong(0);
    private final AtomicLong hits = new AtomicLong(0);
    private final AtomicLong lastRequests = new AtomicLong(0);
    private final AtomicLong lastHits = new AtomicLong(0);
    private volatile boolean capacitySetManually;

    /**
     * 构造函数 默认10M大小
     */
    public LRULinkedStrHashMap() {
        this(CAPACITY, DEFAULT_CONCURRENCY_LEVEL);
    }

    public LRULinkedStrHashMap(int capacity) {
        this(capacity, DEFAULT_CONCURRENCY_LEVEL);
    }

    //Weigher 是一个计算每条记录占用存储单元数接口 使用StringWeigher按字符数组长度计数
    public LRULinkedStrHashMap(int capacity, int concurrency) {
        map = new ConcurrentLinkedHashMap.Builder<K, String>().weigher(new StringWeigher())
                .initialCapacity(capacity).maximumWeightedCapacity(capacity)
                .concurrencyLevel(concurrency).build();
    }

    @Override
    public String put(K key, String value) {
        return map.put(key, value);
    }

    @Override
    public String get(Object key) {
        String v = map.get(key);
        requests.incrementAndGet();
        if (v != null)
            hits.incrementAndGet();
        if (v == null) {
            return null;
        }
        return v;
    }

    public long getCapacity() {
        return map.capacity();
    }

    public boolean isCapacitySetManually() {
        return capacitySetManually;
    }

    public void updateCapacity(int capacity) {
        map.setCapacity(capacity);
    }

    public void setCapacity(int capacity) {
        updateCapacity(capacity);
        capacitySetManually = true;
    }

    public int getSize() {
        return map.size();
    }

    public long getHits() {
        return hits.get();
    }

    public long getRequests() {
        return requests.get();
    }

    public double getRecentHitRate() {
        long r = requests.get();
        long h = hits.get();
        try {
            long i = r - lastRequests.get();
            if (i == 0) {
                return 0;
            }
            return ((double) (h - lastHits.get())) / i;
        } finally {
            lastRequests.set(r);
            lastHits.set(h);
        }
    }

    @Override
    public void clear() {
        map.clear();
        requests.set(0);
        hits.set(0);
    }

    @Override
    public String remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends String> m) {
        if (m != null) {
            for (Entry<? extends K, ? extends String> obj : m.entrySet()) {
                this.put(obj.getKey(), obj.getValue());
            }
        }
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<String> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, String>> entrySet() {
        return map.entrySet();
    }

    public Set<K> getKeySet() {
        return map.keySet();
    }


    private static class StringWeigher implements Weigher<String> {

        @Override
        public int weightOf(String value) {
            return (value.length() << 1);
        }
    }
}