package no.utgdev.sparkly.proxies;

public interface ProxyFactory {
    public <T> T create(T instance, Class<T> type, Class[] argsCls, Object[] args);
}
