package com.weaver.web.annotation;

import java.lang.annotation.*;

/**
 * @description 防止表单重复提交注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Duplicate {

}
