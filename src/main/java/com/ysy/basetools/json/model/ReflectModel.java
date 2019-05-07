package com.ysy.basetools.json.model;

/**
 * Created by guoqiang on 2017/1/3.
 */
public class ReflectModel {
    private final Object o;
    private final String path;

    public String getPath() {
        return path;
    }

    public ReflectModel(Object o, String path) {
        this.o = o;
        this.path = path;
    }

    public Object getO() {
        return o;
    }
}
