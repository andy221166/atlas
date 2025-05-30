package org.atlas.infrastructure.lock.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Lock {

  String key();

  long timeout() default 30;

  TimeUnit timeUnit() default TimeUnit.SECONDS;
}
