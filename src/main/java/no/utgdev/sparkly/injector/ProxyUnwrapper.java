package no.utgdev.sparkly.injector;

public class ProxyUnwrapper<T> {
    public final T proxy;
    public final T object;

    public ProxyUnwrapper(T proxy, T object) {
        this.proxy = proxy;
        this.object = object;
    }
}
