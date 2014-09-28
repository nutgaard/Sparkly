package no.utgdev.proxy;

public class DefaulProxyFactory implements ProxyFactory {

    @Override
    public <T> T create(T instance, Class<T> type) {
        return instance;
    }
}
