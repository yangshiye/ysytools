package com.ysy.basetools.model;


import java.io.Serializable;

/**
 * Created by guoqiang on 2017/11/7.
 */
public class PageParamDTO implements Serializable {
    private Integer startIndex;//从哪个索引之后开始读取数据 不再是原来的pageNum
    private Integer pageSize;//查多少数据

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}