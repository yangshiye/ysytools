package com.ysy.basetools.interceptor;

import com.ysy.basetools.interceptor.wrapper.CommonWrapper;
import com.ysy.basetools.interceptor.wrapper.SingleWrapper;
import com.ysy.basetools.interceptor.wrapper.ThreadLocalWrapper;
import com.ysy.basetools.util.ListUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by guoqiang on 2017/6/9.
 */
public class InterceptorUtil {

    public static <T, R> CommonWrapper<T, R> createCommonWrapper(List<? extends Interceptor<T, R>> list, T t) {
        List<Interceptor<T, R>> temp = new ArrayList<Interceptor<T, R>>(list);
        Collections.reverse(temp);
        CommonWrapper<T, R> result = new CommonWrapper<T, R>(t, null, null);
        Iterator<Interceptor<T, R>> it = temp.iterator();
        do {
            result = new CommonWrapper<T, R>(t, it.next(), result);
        } while (it.hasNext());
        return result;
    }

    public static <T, R> CommonWrapper<T, R> createCommonWrapper(List<? extends Interceptor<T, R>> list, Interceptor<T, R> baseInter, T t) {
        List<Interceptor<T, R>> temp = new ArrayList<Interceptor<T, R>>(ListUtil.size(list) + 1);
        temp.addAll(list);
        temp.add(baseInter);
        return createCommonWrapper(temp, t);
    }

    public static <T, R> SingleWrapper<T, R> createSingleWrapper(List<? extends Interceptor<T, R>> list, T t) {
        SingleWrapper<T, R> result = new SingleWrapper<T, R>(t, list);
        return result;
    }

    public static <T, R> SingleWrapper<T, R> createSingleWrapper(List<? extends Interceptor<T, R>> list, Interceptor<T, R> baseInter, T t) {
        List<Interceptor<T, R>> temp = new ArrayList<Interceptor<T, R>>(ListUtil.size(list) + 1);
        temp.addAll(list);
        temp.add(baseInter);
        return createSingleWrapper(temp, t);
    }


    public static <T, R> ThreadLocalWrapper<T, R> createThreadLocalWrapper(List<? extends Interceptor<T, R>> list, T t) {
        List<Interceptor<T, R>> temp = new ArrayList<Interceptor<T, R>>(list);
        Collections.reverse(temp);
        ThreadLocal<T> threadLocal = new ThreadLocal<T>();
        threadLocal.set(t);
        ThreadLocalWrapper<T, R> result = new ThreadLocalWrapper<T, R>(threadLocal, null, null);
        Iterator<Interceptor<T, R>> it = temp.iterator();
        do {
            result = new ThreadLocalWrapper<T, R>(threadLocal, it.next(), result);
        } while (it.hasNext());
        return result;
    }

    public static <T, R> ThreadLocalWrapper<T, R> createThreadLocalWrapper(List<? extends Interceptor<T, R>> list, Interceptor<T, R> baseInter, T t) {
        List<Interceptor<T, R>> temp = new ArrayList<Interceptor<T, R>>(ListUtil.size(list) + 1);
        temp.addAll(list);
        temp.add(baseInter);
        return createThreadLocalWrapper(temp, t);
    }
}
