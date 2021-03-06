package no.utgdev.sparkly.proxyimpl.mocking;

import no.utgdev.sparkly.proxies.ProxyFactory;

import static org.mockito.Mockito.mock;

public class MockingProxy implements ProxyFactory {

    @SuppressWarnings("unchecked")
    public <T> T create(T instance, Class<T> type, Class[] argsCls, Object[] args) {
        return mock(type);
    }
}
