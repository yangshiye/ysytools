package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSe;
import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

import java.util.*;

/**
 * Created by guoqiang on 2017/10/27.
 */
public class CommonCollectionSerializerFactory extends SerializerFactory<Collection> {
    private static final String UnmodifiableList = "java.util.Collections$UnmodifiableList";
    private static final String UnmodifiableCollection = "java.util.Collections$UnmodifiableCollection";
    private static final String UnmodifiableSet = "java.util.Collections$UnmodifiableSet";
    private static final String UnmodifiableSortedSet = "java.util.Collections$UnmodifiableSortedSet";
    private static final String UnmodifiableRandomAccessList = "java.util.Collections$UnmodifiableRandomAccessList";
    private static final String SynchronizedList = "java.util.Collections$SynchronizedList";
    private static final String SynchronizedCollection = "java.util.Collections$SynchronizedCollection";
    private static final String SynchronizedSet = "java.util.Collections$SynchronizedSet";
    private static final String SynchronizedSortedSet = "java.util.Collections$SynchronizedSortedSet";
    private static final String SynchronizedRandomAccessList = "java.util.Collections$SynchronizedRandomAccessList";
    private final GqSe gqSe;

    @Override
    public void doWriteObj(Collection obj, Output output, GqSeContext context, int depth) {
        context.writeClass(output, obj.getClass());
        output.writeInt(obj.size(), true);
        for (Object temp : obj) {
            gqSe.writeObj(temp, output, context, depth + 1);
        }
    }

    @Override
    public Collection readObj(Input input, GqSeContext context) {
        Class clazz = context.readClass(input, gqSe);
        int len = input.readInt(true);
        Collection collection = createCollection(clazz, context, len);
        for (int i = 0; i < len; i++) {
            collection.add(gqSe.deserialize(input, context));
        }
        return getRealCollection(collection, clazz);
    }


    private Collection createCollection(Class clazz, GqSeContext context, int len) {
        Collection map;
        if (UnmodifiableList.equals(clazz.getName())) {
            map = new LinkedList();
        } else if (UnmodifiableCollection.equals(clazz.getName())) {
            map = new ArrayList(len);
        } else if (UnmodifiableSet.equals(clazz.getName())) {
            map = new HashSet(len);
        } else if (UnmodifiableSortedSet.equals(clazz.getName())) {
            map = new TreeSet();
        } else if (UnmodifiableRandomAccessList.equals(clazz.getName())) {
            map = new ArrayList(len);
        } else if (SynchronizedList.equals(clazz.getName())) {
            map = new LinkedList();
        } else if (SynchronizedCollection.equals(clazz.getName())) {
            map = new ArrayList(len);
        } else if (SynchronizedSet.equals(clazz.getName())) {
            map = new HashSet(len);
        } else if (SynchronizedSortedSet.equals(clazz.getName())) {
            map = new TreeSet();
        } else if (SynchronizedRandomAccessList.equals(clazz.getName())) {
            map = new ArrayList(len);
        } else {
            map = gqSe.createObj(clazz, context);
            if (map instanceof ArrayList) {
                ((ArrayList) map).ensureCapacity(len);
            }
        }

        return map;
    }

    private Collection getRealCollection(Collection map, Class clazz) {
        if (UnmodifiableList.equals(clazz.getName())) {
            map = Collections.unmodifiableList((List) map);
        } else if (UnmodifiableCollection.equals(clazz.getName())) {
            map = Collections.unmodifiableCollection(map);
        } else if (UnmodifiableSet.equals(clazz.getName())) {
            map = Collections.unmodifiableSet((Set) map);
        } else if (UnmodifiableSortedSet.equals(clazz.getName())) {
            map = Collections.unmodifiableSortedSet((TreeSet) map);
        } else if (UnmodifiableRandomAccessList.equals(clazz.getName())) {
            map = Collections.unmodifiableList((List) map);
        } else if (SynchronizedList.equals(clazz.getName())) {
            map = Collections.synchronizedList((List) map);
        } else if (SynchronizedCollection.equals(clazz.getName())) {
            map = Collections.synchronizedCollection(map);
        } else if (SynchronizedSet.equals(clazz.getName())) {
            map = Collections.synchronizedSet((Set) map);
        } else if (SynchronizedSortedSet.equals(clazz.getName())) {
            map = Collections.synchronizedSortedSet((TreeSet) map);
        } else if (SynchronizedRandomAccessList.equals(clazz.getName())) {
            map = Collections.synchronizedList((List) map);
        }

        return map;
    }

    public CommonCollectionSerializerFactory(GqSe gqSe) {
        this.gqSe = gqSe;
    }

}
