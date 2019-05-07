package com.ysy.basetools.gqse;

import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;
import com.ysy.basetools.util.StrUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by guoqiang on 2017/10/19.
 */
public class GqSeContext {
    private boolean needGqSeId;
    private Map<Integer, Class> idToClassMap = null;
    private Map<Class, Integer> classToIdMap = null;
    private AtomicInteger idInc = null;
    private LinkedList<String> seInfoList;
    private boolean convertOpen = false;//是否开启反序列化的值转换功能
    private Integer maxSeObjDepth=150;//最大序列化深度

//    private final IdentityHashMap<Object, Integer> renObjToIdMap = new IdentityHashMap<Object, Integer>(128);
//    private final Map<Integer, Object> renIdToObjMap = new HashMap<Integer, Object>(128);
//    private final AtomicInteger renId = new AtomicInteger(0);

    private Class getClassById(int id) {
        if (idToClassMap == null) {
            return null;
        }
        return idToClassMap.get(id);
    }

    private Integer getIdByClass(Class clazz) {
        if (classToIdMap == null) {
            return null;
        }
        return classToIdMap.get(clazz);
    }

    public Class readClass(Input input, GqSe gqSe) {
        Class clazz;
        String name = input.readString();
        int id = input.readInt(true);
        if (StrUtil.empty(name)) {
            clazz = this.getClassById(id);
        } else {
            clazz = gqSe.getClassByName(name);
            this.putClass(clazz, id);
        }
        return clazz;
    }

    public void writeClass(Output output, Class clazz) {
        Integer id = this.getIdByClass(clazz);
        String name;
        if (id == null) {
            id = this.getNextId();
            this.putClass(clazz, id);
            name = clazz.getName();
        } else {
            name = "";
        }
        output.writeString(name);
        output.writeInt(id, true);

    }

    private void putClass(Class clazz, int id) {
        if (classToIdMap == null) {
            classToIdMap = new HashMap<Class, Integer>();
            idToClassMap = new HashMap<Integer, Class>();
        }
        classToIdMap.put(clazz, id);
        idToClassMap.put(id, clazz);
    }

    private int getNextId() {
        if (idInc == null) {
            idInc = new AtomicInteger(0);
        }
        return idInc.getAndAdd(1);
    }

    @Override
    protected void finalize() {
        this.clear();
    }

    public void clear() {
        idInc = null;
        if (idToClassMap != null) {
            this.idToClassMap.clear();
        }

        if (classToIdMap != null) {
            this.classToIdMap.clear();
        }

        if (seInfoList != null) {
            seInfoList.clear();
        }
    }

    public static GqSeContext buildDefaultContext() {
        GqSeContext context = new GqSeContext();
        context.init(true);
        return context;
    }

    public void init(boolean needGqSeId) {
        this.needGqSeId = needGqSeId;
    }

    private GqSeContext() {
    }

//    public Integer putNextObj(Object obj) {
//        Integer id = renObjToIdMap.get(obj);
//        if (id != null) {
//            return id;
//        } else {
//            int rid = renId.getAndAdd(1);
//            renObjToIdMap.put(obj, rid);
//            return null;
//        }
//    }
//
//    public void putRenObj(Object obj) {
//        renIdToObjMap.put(renId.getAndAdd(1), obj);
//    }
//
//    public Object getObj(int id) {
//        return renIdToObjMap.get(id);
//    }

    public boolean needGqSeId() {
        return needGqSeId;
    }

    public void appSeInfo(Object s) {
        if (seInfoList == null) {
            seInfoList = new LinkedList<String>();
        }
        seInfoList.add(String.valueOf(s));
    }

    public void removeSeInfo() {
        if (seInfoList != null && !seInfoList.isEmpty()) {
            seInfoList.removeLast();
        }
    }

    public String getSeInfo() {
        if (seInfoList == null) {
            return "null";
        }
        return seInfoList.toString();
    }

    public Integer getMaxSeObjDepth() {
        return maxSeObjDepth;
    }

    public void setMaxSeObjDepth(Integer maxSeObjDepth) {
        this.maxSeObjDepth = maxSeObjDepth;
    }

    public void setConvertOpen(boolean convertOpen) {
        this.convertOpen = convertOpen;
    }

    public boolean isConvertOpen() {
        return convertOpen;
    }
}
