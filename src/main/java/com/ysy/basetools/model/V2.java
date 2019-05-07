package com.ysy.basetools.model;

/**
 * Created by guoqiang on 2016/11/8.
 */
public class V2<F1, F2> {
    private F1 v1;
    private F2 v2;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        V2 v21 = (V2) o;

        if (v1 != null ? !v1.equals(v21.v1) : v21.v1 != null) return false;
        return v2 != null ? v2.equals(v21.v2) : v21.v2 == null;
    }

    public static <F1,F2> V2<F1,F2> gen(F1 v1,F2 v2) {
        return new V2<F1,F2>(v1,v2);
    }

    @Override
    public int hashCode() {
        int result = v1 != null ? v1.hashCode() : 0;
        result = 31 * result + (v2 != null ? v2.hashCode() : 0);
        return result;
    }

    public V2() {
    }

    public V2(F1 v1, F2 v2) {

        this.v1 = v1;
        this.v2 = v2;
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
}
