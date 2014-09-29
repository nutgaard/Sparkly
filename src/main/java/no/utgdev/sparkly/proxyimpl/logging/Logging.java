package no.utgdev.sparkly.proxyimpl.logging;

import no.utgdev.sparkly.annotations.ProxyAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@ProxyAnnotation(implementingClass = LoggingProxy.class)
public @interface Logging {
}
