package com.ysy.basetools.timer;

import java.util.concurrent.TimeUnit;

/**
 * Created by guoqiang on 2017/11/7.
 */
@Deprecated
public abstract class NTimerReadValue<V> extends TimerReadValue<Object, V> {

    public NTimerReadValue(long time) {
        super(time);
    }


    public NTimerReadValue(long time, TimeUnit timeUnit) {
        super(time, timeUnit);
    }

    public NTimerReadValue(V v, long timeout) {
        super(v, timeout);
    }

    public NTimerReadValue(V v, long timeout, TimeUnit timeUnit) {
        super(v, timeout, timeUnit);
    }

    public NTimerReadValue(V v) {
        super(v);
    }

}
