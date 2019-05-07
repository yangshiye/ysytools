package com.ysy.basetools.enums;

/**
 * Created by Administrator on 2018/5/9.
 */
public enum ValidStatusEnum implements IntStrEnum {
    VALID(1, "有效"),
    IN_VALID(0, "无效"),;
    private final int code;
    private final String name;

    ValidStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public int getIntCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }
}
