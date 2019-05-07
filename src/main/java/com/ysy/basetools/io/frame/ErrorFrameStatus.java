package com.ysy.basetools.io.frame;

/**
 * Created by Administrator on 2018/12/21.
 */
public class ErrorFrameStatus implements FrameStatus {
    private final String msg;

    @Override
    public String getMsg() {
        return msg;
    }

    public ErrorFrameStatus(String msg) {
        this.msg = msg;
    }
}