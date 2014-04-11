package com.btmatthews.atlas.core.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Component
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface BuilderFactory {

    Class<?> value();
}
