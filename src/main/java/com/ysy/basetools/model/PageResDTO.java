package com.ysy.basetools.model;

/**
 * Created by guoqiang on 2017/12/6.
 */
public class PageResDTO<P,R> extends PageDTO<R> {
    private P param;//查询参数

    public P getParam() {
        return param;
    }

    public void setParam(P param) {
        this.param = param;
    }
}
