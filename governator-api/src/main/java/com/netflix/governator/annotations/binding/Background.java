package com.netflix.governator.annotations.binding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.inject.Qualifier;

@Qualifier
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
/**
 * A generic binding annotation that can be used to specify that something
 * is tied to background processing.  For example, 
 * 
 * bind(ExecutorService.class).annotatedWith(Background.class).toInstance(Executors.newScheduledThreadPool(10));
 * 
 */
public @interface Background
{
}
