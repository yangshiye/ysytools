package com.ysy.basetools.util;

import java.util.*;

/**
 * Created by Administrator on 2018/6/7.
 */
public class ParentChildUtil {

    public static <T, ID> List<T> markTree(Collection<? extends T> list, ParentChild<T, ID> parentChild) {
        if (list == null) {
            return ListUtil.emptyList();
        }
        List<T> root = new LinkedList<>();
        Map<ID, T> map = new HashMap<ID, T>(list.size());
        for (T t : list) {
            if (t != null) {
                ID id = parentChild.getId(t);
                map.put(id, t);
            }
        }
        for (T t : map.values()) {
            ID parentId = parentChild.getParent(t);
            T parent = map.get(parentId);
            if (parent != null) {
                parentChild.addChild(parent, t);
            } else {
                root.add(t);
            }
        }
        return root;
    }


    public static <T, ID> List<T> markTree(Collection<? extends T> list, ParentChildList<T, ID> parentChild) {
        if (list == null) {
            return ListUtil.emptyList();
        }
        List<T> root = new LinkedList<>();
        Map<ID, T> map = new HashMap<ID, T>(list.size());
        for (T t : list) {
            if (t != null) {
                ID id = parentChild.getId(t);
                map.put(id, t);
            }
        }

        Map<ID, List<T>> childrenMap = new HashMap<ID, List<T>>(list.size());
        for (T t : map.values()) {
            ID parentId = parentChild.getParent(t);
            T parent = map.get(parentId);
            if (parent != null) {
                List<T> temp = childrenMap.get(parentId);
                if (temp == null) {
                    temp = new LinkedList<>();
                    childrenMap.put(parentId, temp);
                }
                temp.add(t);
            } else {
                root.add(t);
            }
        }
        for (ID id : childrenMap.keySet()) {
            T t = map.get(id);
            List<T> children = childrenMap.get(id);
            if (t != null && children != null) {
                parentChild.addChildList(t, children);
            }
        }
        return root;
    }

    public interface ParentChildGet<T, ID> {
        ID getId(T t);

        ID getParent(T t);
    }

    public interface ParentChild<T, ID> extends ParentChildGet<T, ID> {
        void addChild(T parent, T child);
    }

    public interface ParentChildList<T, ID> extends ParentChildGet<T, ID> {
        void addChildList(T parent, List<T> child);
    }
}
