package com.ysy.basetools.extfield;

/**
 * Created by Administrator on 2018/5/8.
 */
public interface ExtField<F, O> {
    <T> T convert(F fieldValue, O obj, EF ef);
}
