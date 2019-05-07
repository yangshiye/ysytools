package com.ysy.basetools.model;


import java.util.List;

/**
 * Created by guoqiang on 2017/11/7.
 */
public class PageDTO<R> extends PageParamDTO {
    private List<R> list;//返回数据结果
    private Integer totalCount;//总记录数

    public static <T> PageDTO<T> create(List<T> list) {
        return new PageDTO<T>(list);
    }

    public static <T> PageDTO<T> create(List<T> list, Integer count) {
        return new PageDTO<T>(list, count);
    }

    public PageDTO() {
    }

    public PageDTO(List<R> list) {

        this.list = list;
    }

    public PageDTO(List<R> list, Integer totalCount) {
        this.list = list;
        this.totalCount = totalCount;
    }

    public PageDTO(List<R> list, Integer totalCount, PageParamDTO pageParamDTO) {
        this(list, totalCount);
        if (pageParamDTO != null) {
            this.setPageSize(pageParamDTO.getPageSize());
            this.setStartIndex(pageParamDTO.getStartIndex());
        }
    }

    public PageDTO(List<R> list, Integer totalCount, Integer startIndex, Integer pageSize) {
        this(list, totalCount);
        this.setStartIndex(startIndex);
        this.setPageSize(pageSize);
    }

    public List<R> getList() {
        return list;
    }

    public void setList(List<R> list) {
        this.list = list;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}