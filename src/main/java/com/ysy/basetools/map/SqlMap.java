package com.ysy.basetools.map;

/**
 * Created by guoqiang on 2018/1/11.
 */
public class SqlMap extends SMap<String,Object> {

    public static SqlMap create(Object ...objects){
        if (objects == null) {
            return new SqlMap();
        }
        SqlMap map = new SqlMap((objects.length + 1) / 2);
        for (int i = 0; i < objects.length; i += 2) {
            Object key = objects[i];
            Object value = null;
            if (objects.length > i + 1) {
                value = objects[i + 1];
            }
            if (key == null) {
                map.put(null, value);
            } else if (key instanceof String) {
                map.put((String) key, value);
            } else {
                throw new ClassCastException(key.getClass() + " can not cast to be String");
            }
        }
        return map;
    }

    public SqlMap() {
    }

    public SqlMap(int size) {
        super(size);
    }

    @Override
    public Object get(Object key) {
        if(containsKey(key)){
            return super.get(key);
        }
        throw new RuntimeException("key is not defined!");
    }
}
