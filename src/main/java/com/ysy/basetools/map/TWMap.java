package com.ysy.basetools.map;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by guoqiang on 2017/10/30.
 */
public class TWMap<K, V> {
    private final Map<K, V> key2ValueMap;
    private final Map<V, K> value2KeyMap;

    public int size() {
        return key2ValueMap.size();
    }

    public V put(K key, V value) {
        key2ValueMap.put(key, value);
        value2KeyMap.put(value, key);
        return value;
    }

    public V getValueFromKey(K key) {
        return key2ValueMap.get(key);
    }

    public K getKeyFromValue(V value) {
        return value2KeyMap.get(value);
    }

    public TWMap() {
        key2ValueMap = new HashMap<K, V>();
        value2KeyMap = new HashMap<V, K>();
    }


}
