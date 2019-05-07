package com.ysy.basetools.lrucache;

/**
 * Created by guoqiang on 2016/9/6.
 */
public interface Loader<K, V> {
    V load(K k);
}
