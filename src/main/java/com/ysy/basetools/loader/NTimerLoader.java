package com.ysy.basetools.loader;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/1/30.
 */
public abstract class NTimerLoader<Result> extends TimerLoader<Object, Result> {

    public NTimerLoader(long time) {
        super(time);
    }

    public NTimerLoader(long time, TimeUnit timeUnit) {
        super(time, timeUnit);
    }
}
