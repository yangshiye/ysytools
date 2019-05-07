package com.ysy.basetools.timer;

import com.ysy.basetools.loader.TimerLoader;

import java.util.concurrent.TimeUnit;

/**
 * Created by guoqiang on 2017/11/7.
 */
@Deprecated
public abstract class TimerReadValue<K,V> extends TimerLoader<K, V> {

    public TimerReadValue(long time, boolean firstReadRealValue) {
        super(time);
    }

    public TimerReadValue(long time) {
        super(time);
    }


    public TimerReadValue(long time, TimeUnit timeUnit) {
        super(time, timeUnit);
    }

    public TimerReadValue(V v, long timeout) {
        super(timeout);
        this.setV(v);
    }

    public TimerReadValue(V v, long time, TimeUnit timeUnit) {
        super(time, timeUnit);
        this.setV(v);
    }

    public TimerReadValue(V v) {
        super(10 * 60 * 1000);
        this.setV(v);
    }
}
