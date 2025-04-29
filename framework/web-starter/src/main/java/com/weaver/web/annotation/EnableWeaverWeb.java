package com.weaver.web.annotation;

import com.weaver.web.config.WeaverSecurityAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
@EnableAspectJAutoProxy(exposeProxy = true)
@Import({ WeaverSecurityAutoConfiguration.class })
public @interface EnableWeaverWeb {

}
