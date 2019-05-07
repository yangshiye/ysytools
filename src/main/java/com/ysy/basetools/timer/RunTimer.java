package com.ysy.basetools.timer;

/**
 * Created by guoqiang on 2017/12/5.
 */
public interface RunTimer {

    public long nextStep();

    public void clear();

    public long getTotalTime();

    public int getStepCount();

    public String getRunInfo();
}
