package no.utgdev.annotations;

import no.utgdev.proxy.LoggingProxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@ProxyAnnotation(implementingClass = LoggingProxy.class)
public @interface Logging {
}
