package com.ysy.basetools.job;

/**
 * Created by Administrator on 2017/6/26.
 */
public class JobResult<T, P> {
    private final boolean success;
    private final T result;
    private final P param;

    public static <T, P> JobResult<T, P> suc(T t, P param) {
        return new JobResult<T, P>(true, t, param);
    }

    public static <T, P> JobResult<T, P> err(P param) {
        return new JobResult<T, P>(false, null, param);
    }

    public JobResult(boolean success, T result, P param) {
        this.success = success;
        this.result = result;
        this.param = param;
    }

    public P getParam() {
        return param;
    }

    public boolean isError() {
        return !success;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getResult() {
        return result;
    }
}
