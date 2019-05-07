package com.ysy.basetools.verify;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by guoqiang on 2016/8/8.
 * 验证数据 注解
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Verify {
    String gt() default "";//大于 可用于 byte short int long float double date  默认不检查

    String ge() default "";//大于等于

    String lt() default "";//小于

    String le() default "";//小于等于

    int maxSize() default -1;//最小元素长度 可用于 数组 集合 map string stringbuffer  默认不检查

    int minSize() default -1;//最大元素长度

    boolean notNull() default false;//不能是空

    boolean notEmpty() default false;//不能是空数据 可用于 数据 集合map string

    boolean isNum() default false;//必须是数字 用于String

    boolean nul() default false;//必须是null

    boolean empty() default false;//必须是空数据  可用于 数据 集合map string

    Class instanceFrom() default Object.class;//是class的子类 子接口

    Class eleInstanceFrom() default Object.class;//元素(collection 数组 map的value适用)是class的子类 子接口

    Class keyInstanceFrom() default Object.class;//键(map的key适用)是class的子类子接口

    boolean deepVerify() default false;//深度检查属性

    boolean deepVerifyElement() default false;//深度检查集合 数组中的数据属性

    boolean deepVerifyMapKey() default false;//深度检查map 的key的数据属性

    boolean deepVerifyMapValue() default false;//深度检查map 的value

    boolean deepVerifyNotNull() default false;//深度检查时是否必须不为空 仅对 集合 数组 map有效

    Class<? extends Verifier>[] verifies() default {};//特殊验证类
}
