package com.ysy.basetools.model;


import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.ysy.basetools.ex.ConvertException;
import com.ysy.basetools.thread.ThreadUtil;
import com.ysy.basetools.util.StrUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by guoqiang on 2016/4/23.
 * 通用结果类
 */
public class Res<T> implements Serializable {
    public static final int SUCCESS = 1;
    public static final int ERROR = 0;
    private int code;
    private T re;
    private String msg = "@INIT_UNKNOWN";
    private Throwable e;
    private String detailMsg;
    private final String point;
    private final List<String> pointList;

    public static <T> Res<T> res(Res<T> res) {
        List<String> stackList = createListByRes(res);
        return new Res<T>(res.getCode(), res.getMsg(), res.getRe(), res.getE(), ThreadUtil.lastTrace(), stackList);
    }

    public static <T> Res<T> res(Res res, T t) {
        List<String> stackList = createListByRes(res);
        return new Res<T>(res.getCode(), res.getMsg(), t, res.getE(), ThreadUtil.lastTrace(), stackList);
    }

    public static <T> Res<T> resM(Res<T> res, String msg) {
        List<String> stackList = createListByRes(res);
        return new Res<T>(res.getCode(), msg, res.getRe(), res.getE(), ThreadUtil.lastTrace(), stackList);
    }

    public static <T> Res<T> errRes(Res<T> res) {
        List<String> stackList = createListByRes(res);
        return new Res<T>(ERROR, ResUtil.getMsg(res), ResUtil.getT(res), ResUtil.getE(res), ThreadUtil.lastTrace(), stackList);
    }

    public static <T> Res<T> errResT(Res res, T t) {
        List<String> stackList = createListByRes(res);
        return new Res<T>(ERROR, ResUtil.getMsg(res), t, ResUtil.getE(res), ThreadUtil.lastTrace(), stackList);
    }

    public static <T> Res<T> errRes(Res<T> res, String msg) {
        List<String> stackList = createListByRes(res);
        return new Res<T>(ERROR, msg, ResUtil.getT(res), ResUtil.getE(res), ThreadUtil.lastTrace(), stackList);
    }

    public static <T> Res<T> sucRes(Res<T> res) {
        List<String> stackList = createListByRes(res);
        return new Res<T>(SUCCESS, ResUtil.getMsg(res), ResUtil.getT(res), ResUtil.getE(res), ThreadUtil.lastTrace(), stackList);
    }

    public static <T> Res<T> sucRes(Res res, T t) {
        List<String> stackList = createListByRes(res);
        return new Res<T>(SUCCESS, ResUtil.getMsg(res), t, ResUtil.getE(res), ThreadUtil.lastTrace(), stackList);
    }

    public static <T> Res<T> sucResM(Res<T> res, String msg) {
        List<String> stackList = createListByRes(res);
        return new Res<T>(SUCCESS, msg, ResUtil.getT(res), ResUtil.getE(res), ThreadUtil.lastTrace(), stackList);
    }

    public static <T> Res<T> err(String msg) {
        return new Res<T>(ERROR, msg, null, null, ThreadUtil.lastTrace(), null);
    }

    public static <T> Res<T> res(int code, String msg, T t, Throwable e) {
        return new Res<T>(code, msg, t, e, ThreadUtil.lastTrace(), null);
    }

    public static <T> Res<T> err() {
        return new Res<T>(ERROR, "@INIT_UNKNOWN", null, null, ThreadUtil.lastTrace(), null);
    }

    public static <T> Res<T> err(String msg, T t) {
        return new Res<T>(ERROR, msg, t, null, ThreadUtil.lastTrace(), null);
    }

    public static <T> Res<T> errE(String msg, Throwable e) {
        return new Res<T>(ERROR, msg, null, e, ThreadUtil.lastTrace(), null);
    }

    public static <T> Res<T> errE(String msg, String detailMsg) {
        return new Res<T>(ERROR, msg, null, null, ThreadUtil.lastTrace(), null).setDetailMsg(detailMsg);
    }

    public static <T> Res<T> errE(String msg, Throwable e, String detailMsg) {
        return new Res<T>(ERROR, msg, null, e, ThreadUtil.lastTrace(), null).setDetailMsg(detailMsg);
    }

    public static <T> Res<T> errT(T t) {
        return new Res<T>(ERROR, null, t, null, ThreadUtil.lastTrace(), null);
    }

    public static <T> Res<T> suc(T t) {
        return new Res<T>(SUCCESS, null, t, null, ThreadUtil.lastTrace(), null);
    }


    public static <T> Res<T> suc(String msg, T t) {
        return new Res<T>(SUCCESS, msg, t, null, ThreadUtil.lastTrace(), null);
    }

    public static <T> Res<T> sucT(T t, String msg) {
        return new Res<T>(SUCCESS, msg, t, null, ThreadUtil.lastTrace(), null);
    }

    public static <T> Res<T> suc() {
        return new Res<T>(SUCCESS, null, null, null, ThreadUtil.lastTrace(), null);
    }

    public static <T> Res<T> sucM(String msg) {
        return new Res<T>(SUCCESS, msg, null, null, ThreadUtil.lastTrace(), null);
    }


    public Res(int code, String msg, T t, Throwable e, StackTraceElement element, List<String> list) {
        this.msg = msg;
        this.e = e;
        this.re = t;
        this.code = code;
        this.point = String.valueOf(element);
        if (list != null) {
            this.pointList = Collections.unmodifiableList(list);
        } else {
            this.pointList = Collections.unmodifiableList(new ArrayList<String>(0));
        }
    }

    public Res(int code, String msg, T t, Throwable e, String point, List<String> list) {
        this.msg = msg;
        this.e = e;
        this.re = t;
        this.code = code;
        this.point = point;
        if (list != null) {
            this.pointList = Collections.unmodifiableList(list);
        } else {
            this.pointList = Collections.unmodifiableList(new ArrayList<String>(0));
        }
    }

    @JSONCreator
    private Res(@JSONField(name = "code") int code, @JSONField(name = "msg") String msg,
                @JSONField(name = "re") T re, @JSONField(name = "e") String e, @JSONField(name = "point") String point,
                @JSONField(name = "pointList") List<String> pointList) {
        this.msg = msg;
        if (StrUtil.nEmpty(e)) {
            this.e = new ConvertException(e);
        } else {
            this.e = null;
        }
        this.re = re;
        this.code = code;
        this.point = point;
        if (pointList != null) {
            this.pointList = pointList;
        } else {
            this.pointList = Collections.unmodifiableList(new ArrayList<String>(0));
        }
    }


    public Res(int code, String msg, T t, Throwable e, StackTraceElement element) {
        this(code, msg, t, e, element, null);
    }

    public Res<T> success() {
        this.code = SUCCESS;
        this.msg = "success";
        return this;
    }

    public Res<T> success(String msg) {
        this.code = SUCCESS;
        this.msg = msg;
        return this;
    }

    public Res<T> success(String msg, T t) {
        this.code = SUCCESS;
        this.msg = msg;
        this.re = t;
        return this;
    }

    public Res<T> error() {
        this.code = ERROR;
        this.msg = "error";
        return this;
    }

    public Res<T> error(String msg) {
        this.code = ERROR;
        this.msg = msg;
        return this;
    }

    public Res<T> error(String msg, T t) {
        this.code = ERROR;
        this.msg = msg;
        this.re = t;
        return this;
    }

    public boolean isSuccess() {
        return code == SUCCESS;
    }


    public boolean isSuccessAndReNN() {
        return code == SUCCESS && re != null;
    }

    public boolean isError() {
        return !isSuccess();
    }

    public int getCode() {
        return code;
    }

    public Res<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public T getRe() {
        return re;
    }

    public Res<T> setRe(T re) {
        this.re = re;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Res<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getDetailMsg() {
        return detailMsg;
    }

    public Res<T> setDetailMsg(String detailMsg) {
        this.detailMsg = detailMsg;
        return this;
    }

    public List<String> getPointList() {
        return pointList;
    }

    public String getPoint() {
        return point;
    }

    public Throwable getE() {
        return e;
    }

    public Res<T> setE(Throwable e) {
        this.e = e;
        return this;
    }

    @Override
    public String toString() {
        return "Res{" +
                "code=" + code +
                ", re=" + re +
                ", msg='" + msg + '\'' +
                ", e=" + e +
                ", point=" + point +
                ", pointList=" + pointList +
                '}';
    }

    private static List<String> createListByRes(Res res) {
        List<String> list;
        if (res == null) {
            list = new ArrayList<String>(0);
        } else if (res.pointList != null) {
            list = new ArrayList<String>(res.pointList.size() + 1);
            list.addAll(res.pointList);
            list.add(res.point);
        } else {
            list = new ArrayList<String>(1);
            list.add(res.point);
        }
        return list;
    }
}
