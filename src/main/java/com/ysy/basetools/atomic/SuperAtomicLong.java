package com.ysy.basetools.atomic;

import com.ysy.basetools.util.NumUtil;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by guoqiang on 2017/9/13.
 * 增强原子性操作类
 */
public class SuperAtomicLong extends AtomicLong {
    public static final int JUDE_EQ = 0;
    public static final int JUDE_GT = 1;
    public static final int JUDE_GE = 2;
    public static final int JUDE_LT = 3;
    public static final int JUDE_LE = 4;
    public static final int JUDE_NE = 5;

    public Res operAndInc(int type, long expect, long inc, Long min, Long max) {
        long oldValue = get();
        long newValue = NumUtil.calcRang(oldValue + inc, min, max);
        boolean flag = oper(type, expect, newValue, oldValue);
        return Res.res(flag, oldValue, newValue);
    }

    public Res incByRang(long inc, Long min, Long max) {
        for (; ; ) {
            long oldValue = get();
            long newValue = NumUtil.calcRang(oldValue + inc, min, max);
            if (oldValue == newValue || compareAndSet(oldValue, newValue)) {
                return Res.suc(oldValue, newValue);
            }
        }
    }

    public Res operAndSet(int type, long expect, long newValue) {
        long oldValue = get();
        boolean flag = oper(type, expect, newValue, oldValue);
        return Res.res(flag, oldValue, newValue);
    }

    private boolean oper(int type, long expect, long newValue, long oldValue) {
        if (judge(type, expect, oldValue)) {
            if (oldValue == newValue || compareAndSet(oldValue, newValue)) {
                return true;
            }
        }
        return false;
    }

    public static boolean judge(int type, long expect, long update) {
        switch (type) {
            case JUDE_EQ:
                return update == expect;
            case JUDE_GE:
                return update >= expect;
            case JUDE_GT:
                return update > expect;
            case JUDE_LE:
                return update <= expect;
            case JUDE_LT:
                return update < expect;
            case JUDE_NE:
                return update != expect;
        }
        return false;
    }

    public static class Res {
        private final boolean success;
        private final long oldValue;
        private final long newValue;

        public Res(boolean success, long oldValue, long newValue) {
            this.success = success;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        public boolean isSuccess() {
            return success;
        }

        public long getOldValue() {
            return oldValue;
        }

        public long getNewValue() {
            return newValue;
        }

        public static Res suc(long oldValue, long newValue) {
            return new Res(true, oldValue, newValue);
        }

        public static Res err(long oldValue, long newValue) {
            return new Res(false, oldValue, newValue);
        }

        public static Res res(boolean flag, long oldValue, long newValue) {
            return new Res(flag, oldValue, newValue);
        }
    }
}
