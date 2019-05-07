package com.ysy.basetools.export;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2018/8/16.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    boolean ignore() default false;//是否忽略

    int sortVal() default 0;

    Class cellConvert() default CellConvert.class;//转换方式

    String title() default "";//为空则用属性名做列头

    String extParam() default "";
}
