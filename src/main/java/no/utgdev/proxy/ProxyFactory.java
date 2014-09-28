package no.utgdev.proxy;

public interface ProxyFactory {
    public <T> T create(T instance, Class<T> type);
}
