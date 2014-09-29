package no.utgdev.sparkly.proxyimpl.mocking;

import no.utgdev.sparkly.annotations.AnnotationProcessor;
import no.utgdev.sparkly.proxies.ProxyConfiguration;

public class MockingAnnotationProcessor implements AnnotationProcessor<Mocking> {

    public MockingAnnotationProcessor(){}


    @Override
    public Class<Mocking> accepts() {
        return Mocking.class;
    }

    @Override
    public ProxyConfiguration process(Mocking annotation) {
        try {
            return annotation.configuringClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }
}
