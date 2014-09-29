package no.utgdev.sparkly.annotations;

import no.utgdev.sparkly.proxies.ProxyConfiguration;

import java.lang.annotation.Annotation;

public interface AnnotationProcessor<T extends Annotation> {
    public Class<T> accepts();
    public ProxyConfiguration process(T annotation);
}
