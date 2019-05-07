package com.ysy.basetools.enums;

import com.ysy.basetools.util.CommonUtil;

/**
 * Created by guoqiang on 2017/6/17.
 * 枚举接口方便使用
 */
public interface IEnum<Code, Name> {
    public Code getCode();

    public Name getName();

    default boolean eq(Code code) {
        return CommonUtil.eq(code, this.getCode());
    }

    default boolean neq(Code code) {
        return !eq(code);
    }

    /*

    private final Code code;
    private final Name name;

    IEnum(Code code, Name name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public boolean eq(Code code) {
        return CommonUtil.eq(this.getCode(), code);
    }

    @Override
    public Code getCode() {
        return code;
    }

    @Override
    public Name getName() {
        return name;
    }

    * */

}
