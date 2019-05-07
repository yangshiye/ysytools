package com.ysy.basetools.extfield;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2018/5/8.
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EF {
    String name() default "";//属性名称

    Class extField() default ExtField.class;//转换方式

    boolean override() default false;//是否覆盖该属性

    boolean ignore() default false;//是否忽略该属性

    String extParam() default "";//额外参数
}
