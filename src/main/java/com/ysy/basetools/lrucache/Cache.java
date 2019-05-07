package com.ysy.basetools.lrucache;

import java.util.Map;

/**
 * Created by guoqiang on 2017/6/21.
 * 缓存接口
 */
public interface Cache<K, V> {
    V get(K key);

    boolean supportGetAll();

    Map<K, V> getAll();
}
