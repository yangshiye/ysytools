package com.ysy.basetools.ex;

import com.ysy.basetools.util.StrUtil;

/**
 * Created by Administrator on 2018/4/23.
 */
public class ConvertException extends Throwable {
    public ConvertException(Throwable cause) {
        super("convert ex info = " + getMsg(cause), null);
    }

    public ConvertException(String msg) {
        super("convert error msg = " + msg, null);
    }

    private static String getMsg(Throwable cause) {
        if (cause == null) {
            return "ex is null";
        }
        return StrUtil.toString(cause);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        //不需要当前异常类的堆栈信息 仅用转换前的异常的msg
        return this;
    }
}
