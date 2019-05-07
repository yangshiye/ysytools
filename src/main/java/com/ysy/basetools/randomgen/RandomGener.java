package com.ysy.basetools.randomgen;

/**
 * Created by guoqiang on 2016/7/25.
 */
public interface RandomGener {
    public <T> T getValue(Class<T> currentClass);
}
