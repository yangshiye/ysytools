package com.ysy.basetools.model;

/**
 * Created by Administrator on 2018/5/8.
 */
public class QueryPageParam<P> extends PageParamDTO {
    private P param;

    public P getParam() {
        return param;
    }

    public void setParam(P param) {
        this.param = param;
    }
}
