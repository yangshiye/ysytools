package com.ysy.basetools.verify;


import java.lang.reflect.Field;

/**
 * Created by guoqiang on 2016/8/8.
 * 验证数据接口
 */
public interface Verifier {
    VerifierResult verify(Object currentObj, Object parentObj, Field field);
}
