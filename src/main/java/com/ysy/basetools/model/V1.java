package com.ysy.basetools.model;

/**
 * Created by guoqiang on 2017/6/21.
 */
public class V1<V> {
    private V v;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        V1<?> v1 = (V1<?>) o;

        return v != null ? v.equals(v1.v) : v1.v == null;
    }

    public static <V> V1<V> gen(V v1) {
        return new V1<V>(v1);
    }

    @Override
    public int hashCode() {
        return v != null ? v.hashCode() : 0;
    }

    public V getV() {
        return v;
    }

    public void setV(V v) {
        this.v = v;
    }

    public V1() {

    }

    public V1(V v) {

        this.v = v;
    }
}
