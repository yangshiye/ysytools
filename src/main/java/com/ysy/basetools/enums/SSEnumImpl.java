package com.ysy.basetools.enums;

import com.ysy.basetools.util.CommonUtil;

/**
 * Created by guoqiang on 2017/6/17.
 */
public class SSEnumImpl implements SSEnum {
    private final String code;
    private final String name;

    public SSEnumImpl(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public boolean eq(String code) {
        return CommonUtil.eq(this.getCode(), code);
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }
}
