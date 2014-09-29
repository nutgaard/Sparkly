package no.utgdev.sparkly.factories;

import no.utgdev.sparkly.proxies.ProxyFactory;

import static org.mockito.Mockito.mock;

public class MockingProxy implements ProxyFactory {

    @SuppressWarnings("unchecked")
    public <T> T create(T instance, Class<T> type) {
        return mock(type);
    }
}
