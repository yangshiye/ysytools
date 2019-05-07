package com.ysy.basetools.map;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Administrator on 2018/6/28.
 */
public class AtomicNumMap<K> {
    private final ConcurrentHashMap<K, AtomicLong> map;

    public long inc(K key) {
        return inc(key, 1);
    }

    public long dec(K key) {
        return dec(key, 1);
    }

    public Set<K> keySet() {
        return map.keySet();
    }

    public int size() {
        return map.size();
    }

//    public boolean whenLtSet(long when,long val){
//
//    }
//
//    public boolean whenLeSet(long when,long val){
//
//    }
//
//    public boolean whenGtSet(long when,long val){
//
//    }
//
//    public boolean whenGeSet(long when,long val){
//
//    }
//
//    public boolean whenLtInc(long when,long val){
//
//    }
//
//    public boolean whenLeInc(long when,long val){
//
//    }
//
//    public boolean whenGtInc(long when,long val){
//
//    }
//
//    public boolean whenGeInc(long when,long val){
//
//    }

    public long inc(K key, long value) {
        AtomicLong val = map.get(key);
        if (val == null) {
            val = new AtomicLong(0);
            val = map.putIfAbsent(key, val);
        }
        return val.getAndAdd(value);
    }

    public long dec(K key, long value) {
        AtomicLong val = map.get(key);
        if (val == null) {
            val = new AtomicLong(0);
            val = map.putIfAbsent(key, val);
        }
        return val.getAndAdd(-value);
    }

    public long get(K key) {
        AtomicLong i = map.get(key);
        if (i != null) {
            return i.get();
        }
        return 0;
    }

    public boolean has(K key) {
        return map.containsKey(key);
    }

    public AtomicNumMap(int size) {
        this.map = new ConcurrentHashMap<>(size);
    }

    public AtomicNumMap() {
        this(32);
    }
}
