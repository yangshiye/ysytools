package com.ysy.basetools.clock;

/**
 * Created by guoqiang on 2017/12/18.
 */
public class Clock {
    private boolean run;
    private long lastRunTime;

    public long start() {
        this.run = true;
        this.lastRunTime = System.currentTimeMillis();
        return this.lastRunTime;
    }

    public long stop() {
        long time = this.getRunTime();
        if (this.run) {
            this.run = false;
        }

        return time;
    }

    public long getRunTime() {
        return this.run ? System.currentTimeMillis() - this.lastRunTime : -1L;
    }

    public Clock(boolean run) {
        this.run = run;
        if (run) {
            this.start();
        }

    }

    public boolean isRun() {
        return this.run;
    }

    public long getLastRunTime() {
        return this.lastRunTime;
    }

    public void setLastRunTime(long lastRunTime) {
        this.lastRunTime = lastRunTime;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    public Clock() {
    }

    public static Clock now() {
        Clock c = new Clock();
        c.start();
        return c;
    }
}