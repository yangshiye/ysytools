package com.ysy.basetools.log;

import com.ysy.basetools.thread.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Administrator on 2018/4/23.
 */
public class LogUtil {
    public static final Logger LOG = LoggerFactory.getLogger(LogUtil.class);
    private static final ConcurrentHashMap<String, AtomicLong> delayMap = new ConcurrentHashMap<>(64);
    private static final ConcurrentHashMap<String, AtomicLong> delayCntMap = new ConcurrentHashMap<>(64);

    public static void delayError(int delaySeconds, Object msg) {
        StackTraceElement element = ThreadUtil.lastTrace();
        if (element == null) {
            return;
        }
        String stack = element.toString();
        String str = String.valueOf(msg);
        delayLog(stack, str, delaySeconds * 1000);
    }

    public static void delayError10S(Object msg) {
        StackTraceElement element = ThreadUtil.lastTrace();
        if (element == null) {
            return;
        }
        String stack = element.toString();
        String str = String.valueOf(msg);
        delayLog(stack, str, 10000);
    }

    public static void delayErrorMS(long delayMS, Object msg) {
        StackTraceElement element = ThreadUtil.lastTrace();
        if (element == null) {
            return;
        }
        String stack = element.toString();
        String str = String.valueOf(msg);
        delayLog(stack, str, delayMS);
    }

    private static void delayLog(String stack, String msg, long delayMS) {
        if (delayMS > 0 && stack != null) {
            long nowTime = System.currentTimeMillis();
            AtomicLong atomicTime = delayMap.putIfAbsent(stack, new AtomicLong(nowTime));
            boolean flag = false;
            if (atomicTime == null) {
                flag = true;
            } else {
                long oldTime = atomicTime.get();
                if (oldTime < nowTime - delayMS && atomicTime.compareAndSet(oldTime, nowTime)) {
                    flag = true;
                }
            }
            if (flag) {
                long ignoreCnt = 0;
                AtomicLong cnt = delayCntMap.get(stack);
                if (cnt != null) {
                    ignoreCnt = cnt.get();
                    if (ignoreCnt > 0) {
                        cnt.getAndAdd(-ignoreCnt);
                    }
                }
                String logInfo = "delayLog stack=" + stack + ",ignoreCnt=" + ignoreCnt + ",msg=" + msg;
//                switch (level) {
//                    case ERROR:
                LOG.error(logInfo);
//                        break;
//                    case WARN:
//                        LOG.warn(logInfo);
//                        break;
//                    case INFO:
//                        LOG.info(logInfo);
//                        break;
//                    case DEBUG:
//                        LOG.debug(logInfo);
//                        break;
//                    case TRACE:
//                        LOG.trace(logInfo);
//                        break;
//                }
            } else {
                AtomicLong newCnt = new AtomicLong(0);
                AtomicLong cnt = delayCntMap.putIfAbsent(stack, newCnt);
                if (cnt == null) {
                    cnt = newCnt;
                }
                cnt.incrementAndGet();
            }
        }
    }

    public static Logger getLog(Object obj) {
        if (obj instanceof Class) {
            return LoggerFactory.getLogger((Class) obj);
        } else if (obj instanceof String) {
            return LoggerFactory.getLogger(obj.toString());
        } else if (obj != null && obj.getClass() != null) {
            return LoggerFactory.getLogger(obj.getClass());
        } else {
            return LOG;
        }
    }

    @Deprecated
    public static Logger getLog() {
        try {
            StackTraceElement ele = ThreadUtil.lastTrace();
            if (ele == null || ele.getClassName() == null) {
                return LOG;
            } else {
                return LoggerFactory.getLogger(ele.getClassName());
            }
        } catch (Throwable e) {
            LOG.error("getLog is error!", e);
        }
        return LOG;
    }
}
