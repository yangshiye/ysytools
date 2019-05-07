package com.ysy.basetools.model;

/**
 * Created by guoqiang on 2016/11/8.
 */
public class V4<F1, F2, F3, F4> {
    private F1 v1;
    private F2 v2;
    private F3 v3;
    private F4 v4;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        V4<?, ?, ?, ?> v41 = (V4<?, ?, ?, ?>) o;

        if (v1 != null ? !v1.equals(v41.v1) : v41.v1 != null) return false;
        if (v2 != null ? !v2.equals(v41.v2) : v41.v2 != null) return false;
        if (v3 != null ? !v3.equals(v41.v3) : v41.v3 != null) return false;
        return v4 != null ? v4.equals(v41.v4) : v41.v4 == null;
    }

    public static <F1,F2,F3,F4> V4<F1,F2,F3,F4> gen(F1 v1,F2 v2,F3 v3,F4 v4) {
        return new V4<F1,F2,F3,F4>(v1,v2,v3,v4);
    }

    @Override
    public int hashCode() {
        int result = v1 != null ? v1.hashCode() : 0;
        result = 31 * result + (v2 != null ? v2.hashCode() : 0);
        result = 31 * result + (v3 != null ? v3.hashCode() : 0);
        result = 31 * result + (v4 != null ? v4.hashCode() : 0);
        return result;
    }

    public V4() {
    }

    public V4(F1 v1, F2 v2, F3 v3, F4 v4) {

        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
    }

    public F1 getV1() {
        return v1;
    }

    public void setV1(F1 v1) {
        this.v1 = v1;
    }

    public F2 getV2() {
        return v2;
    }

    public void setV2(F2 v2) {
        this.v2 = v2;
    }

    public F3 getV3() {
        return v3;
    }

    public void setV3(F3 v3) {
        this.v3 = v3;
    }

    public F4 getV4() {
        return v4;
    }

    public void setV4(F4 v4) {
        this.v4 = v4;
    }
}
