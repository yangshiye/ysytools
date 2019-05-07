package com.ysy.basetools.enums;

/**
 * Created by Administrator on 2018/5/9.
 */
public enum BoolEnum implements IntStrEnum {
    YES(1, "是"),
    NO(0, "否"),;
    private final int code;
    private final String name;

    BoolEnum(int code, String name) {
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
