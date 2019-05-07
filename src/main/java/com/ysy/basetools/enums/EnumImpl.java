package com.ysy.basetools.enums;

import com.ysy.basetools.util.CommonUtil;

/**
 * Created by guoqiang on 2017/6/17.
 */
public class EnumImpl<Code, Name> implements IEnum<Code, Name> {
    private final Code code;
    private final Name name;

    public EnumImpl(Code code, Name name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Code getCode() {
        return code;
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public boolean eq(Code code) {
        return CommonUtil.eq(this.getCode(), code);
    }
}
