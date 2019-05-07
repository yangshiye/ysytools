package com.ysy.basetools;

import com.ysy.basetools.enums.IEnum;

public enum SexEnum implements IEnum {

    MALE(1, "男"),
    FEMALE(2, "女");

    private int code;
    private String text;

    SexEnum(int code, String text) {
        this.code = code;
        this.text = text;
    }

    @Override
    public Object getCode() {
        return code;
    }

    @Override
    public Object getName() {
        return text;
    }
}
