package com.weaver.db.annotation;

import org.springframework.context.annotation.ComponentScan;

import java.lang.annotation.*;

/**
 * @Author: zh
 * @CreateTime: 2025-04-12
 * @Description: 启动缓存
 * @Version: 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
@ComponentScan(basePackages = { "com.weaver.db" })
public @interface EnableWeaverDb {
}
