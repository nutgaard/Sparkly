package no.utgdev.sparkly.factories;

public interface ProxyFactory {
    public <T> T create(T instance, Class<T> type);
}
