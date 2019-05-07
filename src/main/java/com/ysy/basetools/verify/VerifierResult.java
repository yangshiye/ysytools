package com.ysy.basetools.verify;

import com.ysy.basetools.thread.ThreadUtil;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2017/1/30.
 */
public class VerifierResult {
    public static final int SUCCESS = 1;
    public static final int ERROR = 0;
    private int code;
    private Field field;
    private String msg = "@INIT_UNKNOWN";
    private Throwable e;
    private final StackTraceElement stack;
    private String detailMsg;

    public VerifierResult(StackTraceElement stack) {
        this.stack = stack;
    }

    public VerifierResult(int code, String msg) {
        this.stack = ThreadUtil.lastTrace();
        this.code = code;
        this.msg = msg;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Field getField() {
        return field;
    }

    public boolean isError() {
        return code != SUCCESS;
    }

    public VerifierResult error(Field field, String s) {
        this.msg = s;
        this.code = ERROR;
        return this;
    }

    public static VerifierResult err(String s) {
        return new VerifierResult(ERROR, s);
    }

    public void setE(Exception e) {
        this.e = e;
    }

    public Throwable getE() {
        return e;
    }

    public void setE(Throwable e) {
        this.e = e;
    }

    public boolean isSuccess() {
        return code == SUCCESS;
    }

    public String getMsg() {
        return msg;
    }
}
