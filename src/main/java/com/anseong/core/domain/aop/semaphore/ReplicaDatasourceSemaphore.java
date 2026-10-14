package com.anseong.core.domain.aop.semaphore;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ReplicaDatasourceSemaphore {

     long timeout() default 10;

     TimeUnit unit() default TimeUnit.SECONDS;
}
