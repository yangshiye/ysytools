package com.ysy.basetools.model;

import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.ysy.basetools.ex.ConvertException;
import com.ysy.basetools.thread.ThreadUtil;
import com.ysy.basetools.util.StrUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/6/15.
 */
@Deprecated
public class Result<T> implements Serializable {
    public static final int SUCCESS = 1;
    public static final int ERROR = 0;
    private int code;
    private T data;
    private String msg = "@INIT_UNKNOWN";
    private Throwable e;
    private String detailMsg;

    public static <T> Result<T> res(Result<T> res) {
        return new Result<T>(res.getCode(), res.getMsg(), res.getData(), res.getE(), ThreadUtil.lastTrace());
    }

    public static <T> Result<T> res(Result res, T t) {
        return new Result<T>(res.getCode(), res.getMsg(), t, res.getE(), ThreadUtil.lastTrace());
    }

    public static <T> Result<T> resM(Result<T> res, String msg) {
        return new Result<T>(res.getCode(), msg, res.getData(), res.getE(), ThreadUtil.lastTrace());
    }

    public static <T> Result<T> errRes(Result<T> res) {
        return new Result<T>(ERROR, ResUtil.getMsg(res), ResUtil.getT(res), ResUtil.getE(res), ThreadUtil.lastTrace());
    }

    public static <T> Result<T> errResT(Result res, T t) {
        return new Result<T>(ERROR, ResUtil.getMsg(res), t, ResUtil.getE(res), ThreadUtil.lastTrace());
    }

    public static <T> Result<T> errRes(Result<T> res, String msg) {
        return new Result<T>(ERROR, msg, ResUtil.getT(res), ResUtil.getE(res), ThreadUtil.lastTrace());
    }

    public static <T> Result<T> sucRes(Result<T> res) {
        return new Result<T>(SUCCESS, ResUtil.getMsg(res), ResUtil.getT(res), ResUtil.getE(res), ThreadUtil.lastTrace());
    }

    public static <T> Result<T> sucRes(Result res, T t) {
        return new Result<T>(SUCCESS, ResUtil.getMsg(res), t, ResUtil.getE(res), ThreadUtil.lastTrace());
    }

    public static <T> Result<T> sucResM(Result<T> res, String msg) {
        return new Result<T>(SUCCESS, msg, ResUtil.getT(res), ResUtil.getE(res), ThreadUtil.lastTrace());
    }

    public static <T> Result<T> err(String msg) {
        return new Result<T>(ERROR, msg, null, null, ThreadUtil.lastTrace(), null);
    }

    public static <T> Result<T> res(int code, String msg, T t, Throwable e) {
        return new Result<T>(code, msg, t, e, ThreadUtil.lastTrace(), null);
    }

    public static <T> Result<T> err() {
        return new Result<T>(ERROR, "@INIT_UNKNOWN", null, null, ThreadUtil.lastTrace(), null);
    }

    public static <T> Result<T> err(String msg, T t) {
        return new Result<T>(ERROR, msg, t, null, ThreadUtil.lastTrace(), null);
    }

    public static <T> Result<T> errE(String msg, Throwable e) {
        return new Result<T>(ERROR, msg, null, e, ThreadUtil.lastTrace(), null);
    }

    public static <T> Result<T> errE(String msg, String detailMsg) {
        return new Result<T>(ERROR, msg, null, null, ThreadUtil.lastTrace(), null).setDetailMsg(detailMsg);
    }

    public static <T> Result<T> errE(String msg, Throwable e, String detailMsg) {
        return new Result<T>(ERROR, msg, null, e, ThreadUtil.lastTrace(), null).setDetailMsg(detailMsg);
    }

    public static <T> Result<T> errT(T t) {
        return new Result<T>(ERROR, null, t, null, ThreadUtil.lastTrace(), null);
    }

    public static <T> Result<T> suc(T t) {
        return new Result<T>(SUCCESS, null, t, null, ThreadUtil.lastTrace(), null);
    }


    public static <T> Result<T> suc(String msg, T t) {
        return new Result<T>(SUCCESS, msg, t, null, ThreadUtil.lastTrace(), null);
    }

    public static <T> Result<T> sucT(T t, String msg) {
        return new Result<T>(SUCCESS, msg, t, null, ThreadUtil.lastTrace(), null);
    }

    public static <T> Result<T> suc() {
        return new Result<T>(SUCCESS, null, null, null, ThreadUtil.lastTrace(), null);
    }

    public static <T> Result<T> sucM(String msg) {
        return new Result<T>(SUCCESS, msg, null, null, ThreadUtil.lastTrace(), null);
    }


    public Result(int code, String msg, T t, Throwable e, StackTraceElement element, List<String> list) {
        this.msg = msg;
        this.e = e;
        this.data = t;
        this.code = code;
    }

    public Result(int code, String msg, T t, Throwable e, String point, List<String> list) {
        this.msg = msg;
        this.e = e;
        this.data = t;
        this.code = code;
    }

    @JSONCreator
    private Result(@JSONField(name = "code") int code, @JSONField(name = "msg") String msg,
                   @JSONField(name = "re") T re, @JSONField(name = "e") String e, @JSONField(name = "point") String point,
                   @JSONField(name = "pointList") List<String> pointList) {
        this.msg = msg;
        if (StrUtil.nEmpty(e)) {
            this.e = new ConvertException(e);
        } else {
            this.e = null;
        }
        this.data = re;
        this.code = code;
    }


    public Result(int code, String msg, T t, Throwable e, StackTraceElement element) {
        this(code, msg, t, e, element, null);
    }

    public Result<T> success() {
        this.code = SUCCESS;
        this.msg = "success";
        return this;
    }

    public Result<T> success(String msg) {
        this.code = SUCCESS;
        this.msg = msg;
        return this;
    }

    public Result<T> success(String msg, T t) {
        this.code = SUCCESS;
        this.msg = msg;
        this.data = t;
        return this;
    }

    public Result<T> error() {
        this.code = ERROR;
        this.msg = "error";
        return this;
    }

    public Result<T> error(String msg) {
        this.code = ERROR;
        this.msg = msg;
        return this;
    }

    public Result<T> error(String msg, T t) {
        this.code = ERROR;
        this.msg = msg;
        this.data = t;
        return this;
    }

    public boolean isSuccess() {
        return code == SUCCESS;
    }


    public boolean isSuccessAndReNN() {
        return code == SUCCESS && data != null;
    }

    public boolean isError() {
        return !isSuccess();
    }

    public int getCode() {
        return code;
    }

    public Result<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public T getData() {
        return data;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Result<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getDetailMsg() {
        return detailMsg;
    }

    public Result<T> setDetailMsg(String detailMsg) {
        this.detailMsg = detailMsg;
        return this;
    }

    public Throwable getE() {
        return e;
    }

    public Result<T> setE(Throwable e) {
        this.e = e;
        return this;
    }

    @Override
    public String toString() {
        return "Res{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                ", e=" + e +
                '}';
    }
}
