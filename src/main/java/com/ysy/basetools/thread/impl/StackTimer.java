package com.ysy.basetools.thread.impl;

import com.ysy.basetools.thread.ThreadUtil;
import com.ysy.basetools.timer.RunTimer;

import java.util.LinkedList;

/**
 * Created by guoqiang on 2017/12/5.
 */
public class StackTimer implements RunTimer {
    private static final ThreadLocal<StackTimer> TIMER_THREAD_LOCAL = new ThreadLocal<StackTimer>();
    private final LinkedList<Info> list = new LinkedList<Info>();
    private long lastTime = System.currentTimeMillis();
    private long totalTime = 0;

    public static void next() {
        StackTimer timer = TIMER_THREAD_LOCAL.get();
        if (timer == null) {
            get();
        } else {
            timer.nextStep();
        }
    }

    public static StackTimer get() {
        StackTimer timer = TIMER_THREAD_LOCAL.get();
        if (timer == null) {
            timer = new StackTimer();
            TIMER_THREAD_LOCAL.set(timer);
        }
        return timer;
    }

    public static void remove() {
        TIMER_THREAD_LOCAL.remove();
    }

    @Override
    public String getRunInfo() {
        StringBuilder builder = new StringBuilder(1024);
        for (Info info : list) {
            builder.append(info.element).append("[").append(info.time).append("],");
        }
        return "{total:" + totalTime + "info=" + builder + "}";
    }

    @Override
    public long nextStep() {
        long now = System.currentTimeMillis();
        long time = now - lastTime;
        Info info = new Info(ThreadUtil.lastClassTrace(), time);
        list.add(info);
        lastTime = now;
        totalTime += time;
        return totalTime;
    }

    public StackTimer() {
        list.add(new Info(ThreadUtil.lastClassTrace(), 0));
    }

    @Override
    public void clear() {
        list.clear();
        totalTime = 0;
        list.add(new Info(ThreadUtil.lastTrace(), 0));
        lastTime = System.currentTimeMillis();
    }

    @Override
    public long getTotalTime() {
        return totalTime;
    }

    @Override
    public int getStepCount() {
        return list.size();
    }

    private static class Info {
        private final StackTraceElement element;
        private final long time;

        private Info(StackTraceElement element, long time) {
            this.element = element;
            this.time = time;
        }
    }
}
