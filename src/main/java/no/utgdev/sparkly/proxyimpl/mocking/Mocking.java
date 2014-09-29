package no.utgdev.sparkly.proxyimpl.mocking;

import no.utgdev.sparkly.annotations.ProxyAnnotation;
import no.utgdev.sparkly.proxyimpl.DefaultProxyConfiguration;
import no.utgdev.sparkly.proxies.ProxyConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@ProxyAnnotation(implementingClass = MockingProxy.class)
public @interface Mocking {
    Class<? extends ProxyConfiguration> configuringClass() default DefaultProxyConfiguration.class;
}
