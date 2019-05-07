package com.ysy.basetools.enums;

import com.ysy.basetools.util.CommonUtil;

/**
 * Created by guoqiang on 2017/6/17.
 */
@Deprecated
public class ISEnumImpl implements ISEnum {
    private final Integer code;
    private final String name;

    public ISEnumImpl(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public boolean eq(Integer code) {
        return CommonUtil.eq(this.getCode(), code);
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }
}
