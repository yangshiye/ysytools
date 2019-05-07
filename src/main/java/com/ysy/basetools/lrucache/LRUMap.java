package com.ysy.basetools.lrucache;

import com.ysy.basetools.util.CommonUtil;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by guoqiang on 2017/5/3.
 * 最近使用map
 */
public class LRUMap<K, V> implements Map<K, V> {
    private final int maxSize;
    private final Map<K, LRUModel<K, V>> map;
    private final Long timeout;
    private LRUNode<K> head;
    private LRUNode<K> end;

    @Override
    public synchronized V get(Object key) {
        LRUModel<K, V> temp = map.get(key);
        if (temp == null) {
            return null;
        } else if (temp.invalid()) {
            map.remove(key);
            removeNode(temp.getNode());
            return null;
        } else {
            LRUNode<K> node = temp.getNode();
            nodeToEnd(node);
            return temp.getV();
        }
    }

    protected synchronized LRUModel<K, V> doGet(Object key) {
        return map.get(key);
    }

    @Override
    public synchronized V put(K key, V value) {
        LRUModel<K, V> temp = map.get(key);
        if (temp != null) {
            V v = null;
            if (temp.valid()) {
                v = temp.getV();
            }
            temp.setV(value);
            Long time = calcDeathTime(timeout);
            temp.setDeathTime(time);
            LRUNode<K> node = temp.getNode();
            nodeToEnd(node);
            return v;
        } else {
            while (map.size() >= maxSize && head != null) {
                this.remove(head.getKey());
            }
            LRUNode<K> node = new LRUNode<K>(key);
            Long time = calcDeathTime(timeout);
            LRUModel<K, V> model = new LRUModel<K, V>(key, value, node, time);
            addNode(node);
            map.put(key, model);
            return null;
        }
    }

    @Override
    public synchronized V remove(Object key) {
        LRUModel<K, V> v = map.remove(key);
        if (v != null) {
            LRUNode<K> node = v.getNode();
            removeNode(node);
            return v.getV();
        }
        return null;
    }

    @Override
    public synchronized void clear() {
        map.clear();
        head = null;
        end = null;
    }


    @Override
    public synchronized int size() {
        return map.size();
    }

    @Override
    public synchronized boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public synchronized boolean containsKey(Object key) {
        LRUModel<K, V> value = map.get(key);
        if (value == null) {
            return false;
        } else if (value.invalid()) {
            this.remove(key);
            return false;
        }
        return true;
    }

    @Override
    @Deprecated
    public synchronized boolean containsValue(Object value) {
        for (LRUNode<K> node = head; node != null; node = node.getNext()) {
            LRUModel<K, V> temp = map.get(node.getKey());
            if (temp != null && temp.valid()) {
                V v = temp.getV();
                if (CommonUtil.eq(v, value)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public synchronized void putAll(Map<? extends K, ? extends V> m) {
        if (m != null) {
            for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
                this.put(entry.getKey(), entry.getValue());
                if (this.size() >= maxSize) {
                    return;
                }
            }
        }
    }

    @Override
    public synchronized Set<K> keySet() {
        Set<K> result = new HashSet<K>(map.size());
        for (LRUNode<K> node = head; node != null; node = node.getNext()) {
            LRUModel<K, V> value = map.get(node.getKey());
            if (value != null && value.valid()) {
                result.add(node.getKey());
            }
        }
        return result;
    }

    @Override
    public synchronized Collection<V> values() {
        List<V> result = new ArrayList<V>(map.size());
        for (LRUNode<K> node = head; node != null; node = node.getNext()) {
            LRUModel<K, V> temp = map.get(node.getKey());
            if (temp != null && temp.valid()) {
                V v = temp.getV();
                result.add(v);
            }
        }
        return result;
    }

    @Override
    public synchronized Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> result = new HashSet<Entry<K, V>>(map.size());
        for (LRUNode<K> node = head; node != null; node = node.getNext()) {
            LRUModel<K, V> temp = map.get(node.getKey());
            if (temp != null && temp.valid()) {
                V v = temp.getV();
                result.add(new HashMap.SimpleEntry<K, V>(node.getKey(), v));
            }
        }
        return result;
    }

    private void removeNode(LRUNode<K> node) {
        LRUNode<K> last = node.getLast();
        LRUNode<K> next = node.getNext();
        if (last == null) {
            head = next;
            if (head == null) {
                end = null;
            } else {
                head.setLast(null);
            }
        } else if (next == null) {
            end = last;
            end.setNext(null);
        } else {
            last.setNext(next);
            next.setLast(last);
        }
    }

    private void addNode(LRUNode<K> node) {
        if (head == null) {
            node.setLast(null);
            node.setNext(null);
            head = node;
            end = node;
        } else {
            end.setNext(node);
            node.setLast(end);
            node.setNext(null);
            end = node;
        }
    }

    private void nodeToEnd(LRUNode<K> node) {
        if (node != end) {
            removeNode(node);
            addNode(node);
        }
    }

    public LRUMap(int size) {
        if (size < 0) {
            size = 100;
        }
        this.maxSize = size;
        map = new HashMap<K, LRUModel<K, V>>(maxSize);
        head = null;
        end = null;
        timeout = null;
    }

    public LRUMap(int size, long time, TimeUnit unit) {
        if (size < 0) {
            size = 100;
        }
        this.maxSize = size;
        map = new HashMap<K, LRUModel<K, V>>(maxSize);
        head = null;
        end = null;
        timeout = unit.toMillis(time);
    }

    private static class LRUNode<K> {
        private LRUNode<K> last;
        private LRUNode<K> next;
        private final K key;

        private LRUNode(K key) {
            this.key = key;
        }

        private K getKey() {
            return key;
        }

        private LRUNode<K> getLast() {
            return last;
        }

        private void setLast(LRUNode<K> last) {
            this.last = last;
        }

        private LRUNode<K> getNext() {
            return next;
        }

        private void setNext(LRUNode<K> next) {
            this.next = next;
        }
    }

    protected static class LRUModel<K, V> {
        private final K key;
        private V v;
        private final LRUNode<K> node;
        private Long deathTime;

        private LRUModel(K key, V value, LRUNode<K> node, Long deathTime) {
            this.key = key;
            v = value;
            this.node = node;
            this.deathTime = deathTime;
        }

        protected boolean valid() {
            Long time = deathTime;
            if (time == null) {
                return true;
            }
            return time > System.currentTimeMillis();
        }

        protected boolean invalid() {
            Long time = deathTime;
            if (time == null) {
                return false;
            }
            return time <= System.currentTimeMillis();
        }

        private void setDeathTime(Long time) {
            this.deathTime = time;
        }

        private LRUNode<K> getNode() {
            return node;
        }

        protected V getV() {
            return v;
        }

        private void setV(V v) {
            this.v = v;
        }

        private K getKey() {
            return key;
        }
    }

    private static Long calcDeathTime(Long time) {
        if (time == null) {
            return null;
        }
        return System.currentTimeMillis() + time;
    }
}
