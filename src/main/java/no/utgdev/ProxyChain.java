package no.utgdev;

import no.utgdev.proxy.ProxyFactory;

import java.util.ArrayList;
import java.util.List;

public class ProxyChain {
    private List<ProxyFactory> handlers = new ArrayList<>();

    public static ProxyChain start() {
        return new ProxyChain();
    }

    public ProxyChain with(ProxyFactory handler) {
        this.handlers.add(handler);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T build(T instance, Class<T> type) {
        try {
            T proxy = instance;
            for (ProxyFactory pf : handlers) {
                proxy = pf.create(proxy, type);
            }
            return proxy;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
