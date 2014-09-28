package no.utgdev.sparkly.annotations.interceptors;

import no.utgdev.sparkly.factories.LoggingProxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@ProxyAnnotation(implementingClass = LoggingProxy.class)
public @interface Logging {
}
