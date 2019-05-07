package com.ysy.basetools.json.model;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Created by guoqiang on 2017/1/3.
 */
public class ReflectMap {
    private final Map<Object, ReflectModel> map = new IdentityHashMap<Object, ReflectModel>();

    public ReflectModel get(Object o) {
        return map.get(o);
    }

    public String getPath(Object o) {
        ReflectModel model = map.get(o);
        if (model == null) {
            return null;
        }
        return model.getPath();
    }

    public void putPath(Object o, String path) {
        ReflectModel model = new ReflectModel(o, path);
        map.put(o, model);
    }

    public void clear() {
        map.clear();
    }

    public void remove(Object o) {
        map.remove(o);
    }
}
