package no.utgdev.sparkly.proxyimpl.caching;

import no.utgdev.sparkly.annotations.AnnotationProcessor;
import no.utgdev.sparkly.proxies.ProxyConfiguration;

public class CachingAnnotationProcessor implements AnnotationProcessor<Caching> {

    public CachingAnnotationProcessor(){}


    @Override
    public Class<Caching> accepts() {
        return Caching.class;
    }

    @Override
    public ProxyConfiguration process(Caching annotation) {
        try {
            return annotation.configuringClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }
}
