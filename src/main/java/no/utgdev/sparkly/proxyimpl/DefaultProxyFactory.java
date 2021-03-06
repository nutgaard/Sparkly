package no.utgdev.sparkly.proxyimpl;

import no.utgdev.sparkly.proxies.ProxyFactory;

public class DefaultProxyFactory implements ProxyFactory {

    @Override
    public <T> T create(T instance, Class<T> type, Class[] argsCls, Object[] args) {
        return instance;
    }
}
