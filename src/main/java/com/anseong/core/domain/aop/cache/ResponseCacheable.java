package com.anseong.core.domain.aop.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseCacheable {

     String type() default "";

     int page() default -1;

     long timeoutMinutes() default 1;

     // String[] parameterNames() default {};
}
