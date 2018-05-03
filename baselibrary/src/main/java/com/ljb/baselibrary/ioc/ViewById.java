package com.ljb.baselibrary.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author      :ljb
 * Date        :2018/4/5
 * Description : View findviewbyid注解的Annotation
 */
@Target(ElementType.FIELD)//作用于属性
@Retention(RetentionPolicy.RUNTIME)//运行时生效
public @interface ViewById {
    int value();
}
