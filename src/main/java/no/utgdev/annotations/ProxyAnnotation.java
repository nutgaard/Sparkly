package no.utgdev.annotations;

import no.utgdev.proxy.DefaulProxyFactory;
import no.utgdev.proxy.ProxyFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ProxyAnnotation {
    Class<? extends ProxyFactory> implementingClass() default DefaulProxyFactory.class;
}
