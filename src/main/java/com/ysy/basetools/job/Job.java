package com.ysy.basetools.job;

/**
 * Created by Administrator on 2017/6/26.
 */
public interface Job<T,P> {
    T doJob(P p);
}
