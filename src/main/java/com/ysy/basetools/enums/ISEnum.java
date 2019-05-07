package com.ysy.basetools.enums;

/**
 * Created by guoqiang on 2017/6/17.
 */
@Deprecated//后续使用IntStrEnum
public interface ISEnum extends IEnum<Integer, String> {

    /*
    private final int code;
    private final String name;

    ISEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }
    * */
}
