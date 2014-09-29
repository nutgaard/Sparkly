package no.utgdev.sparkly.annotations.interceptors;

import no.utgdev.sparkly.factories.DefaultProxyConfiguration;
import no.utgdev.sparkly.factories.DefaultProxyFactory;
import no.utgdev.sparkly.proxies.ProxyFactory;
import no.utgdev.sparkly.proxies.ProxyConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ProxyAnnotation {
    Class<? extends ProxyFactory> implementingClass() default DefaultProxyFactory.class;
    Class<? extends ProxyConfiguration> configuringClass() default DefaultProxyConfiguration.class;
}
