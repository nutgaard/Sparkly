package no.utgdev.sparkly.annotations.interceptors;

import no.utgdev.sparkly.factories.DefaulProxyFactory;
import no.utgdev.sparkly.factories.ProxyFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ProxyAnnotation {
    Class<? extends ProxyFactory> implementingClass() default DefaulProxyFactory.class;
}
