package com.ysy.basetools.gqse.exceptions;

/**
 * Created by guoqiang on 2017/11/10.
 */
public class GqSeEx extends RuntimeException {
    public GqSeEx() {
        super();
    }

    public GqSeEx(String message) {
        super(message);
    }

    public GqSeEx(String message, Throwable cause) {
        super(message, cause);
    }

    public GqSeEx(Throwable cause) {
        super(cause);
    }
}
