package com.ysy.basetools.thread;


import com.ysy.basetools.util.ReflectUtil;

import java.util.concurrent.ExecutorService;

/**
 * Created by guoqiang on 2015/06/02.
 */
public final class ThreadUtil {

    public static final int THREAD_POOL_COUNT = 5;

    public static void shutdownAndWaitRunOver(ExecutorService executor) {
        shutdownAndWaitRunOver(executor, 10);
    }

    /**
     * 停止线程池,并等待线程池中的任务跑完
     *
     * @param executor
     * @param time
     */
    public static void shutdownAndWaitRunOver(ExecutorService executor, long time) {
        executor.shutdown();
        while (!executor.isTerminated()) {
            sleep(time);
        }
    }

    /**
     * 停止线程池,并等待线程池中的任务跑完
     *
     * @param executor
     * @param time
     */
    public static boolean shutdownAndWaitRunOver(ExecutorService executor, long time, long timeout) {
        long startTime = System.currentTimeMillis();
        executor.shutdown();
        while (!executor.isTerminated() && startTime > System.currentTimeMillis() - timeout) {
            sleep(time);
        }
        return executor.isTerminated();
    }

    public static InterruptedException sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            return e;
        }
        return null;
    }

    public static String getTraceInfo() {
        StringBuilder builder = new StringBuilder(2048);
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 2; i < stackTrace.length; i++) {
            builder.append(stackTrace[i].toString()).append("\n");
        }
        return builder.toString();
    }

    public static StackTraceElement currentTrace() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        int i;
        for (i = 2; i > -1 && i < stackTrace.length; i--) {
            return stackTrace[i];
        }
        return null;
    }

    public static StackTraceElement lastTrace() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        int i;
        for (i = 3; i > -1 && i < stackTrace.length; i--) {
            return stackTrace[i];
        }
        return null;
    }

    public static StackTraceElement lastClassTrace() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length < 3) {
            return stackTrace[stackTrace.length - 1];
        }
        StackTraceElement lastClass = stackTrace[2];
        for (int i = 3; i < stackTrace.length; i++) {
            if (!lastClass.getClassName().equals(stackTrace[i].getClassName())) {
                return stackTrace[i];
            }
        }
        return stackTrace[0];
    }

    /**
     * 获取堆栈中clazz以上的一个
     *
     * @param stackTrace
     * @param clazz
     * @return
     */
    public static StackTraceElement getLastStackTraceElement(StackTraceElement[] stackTrace, Class clazz) {
        for (StackTraceElement element : stackTrace) {
            if (!element.getClassName().equals(clazz.getName())) {
                return element;
            }
        }
        if (stackTrace.length > 0) {
            return stackTrace[0];
        }
        return null;
    }

    /**
     * 从栈底开始找到第一个是当前项目的类的堆栈索引
     *
     * @param stacks
     * @return
     */
    public static int findCurStack(StackTraceElement[] stacks) {
        if (stacks == null) {
            return -1;
        }
        for (int i = stacks.length - 1; i > -1; i--) {
            StackTraceElement element = stacks[i];
            if (element != null) {
                if (ReflectUtil.isFileClass(element.getClassName())) {
                    return i;
                }
            }
        }
        return -1;
    }
}
