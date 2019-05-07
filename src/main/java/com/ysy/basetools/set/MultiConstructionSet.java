package com.ysy.basetools.set;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by guoqiang on 2017/8/1.
 */
@Deprecated
public class MultiConstructionSet extends HashSet<String> {
    public MultiConstructionSet() {
    }


    public MultiConstructionSet(List<Object> list) {
        this.addObj(list);
    }

    private void addObj(Object obj) {
        if (obj instanceof String) {
            this.add((String) obj);
        } else if (obj instanceof Collection) {
            for (Object temp : ((Collection) obj)) {
                this.addObj(temp);
            }
        }
    }

    public MultiConstructionSet(Set<String> c) {
        super(c);
    }
}
