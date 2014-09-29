package no.utgdev.sparkly.factories;

import no.utgdev.sparkly.proxies.ProxyFactory;

public class DefaultProxyFactory implements ProxyFactory {

    @Override
    public <T> T create(T instance, Class<T> type) {
        return instance;
    }
}
