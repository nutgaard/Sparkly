package no.utgdev.sparkly.factories;

public class DefaulProxyFactory implements ProxyFactory {

    @Override
    public <T> T create(T instance, Class<T> type) {
        return instance;
    }
}
