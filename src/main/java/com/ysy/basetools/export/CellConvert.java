package com.ysy.basetools.export;

/**
 * Created by Administrator on 2018/8/16.
 */
public interface CellConvert<F, O> {
    String convert(F fieldValue, O obj, Column column);
}
