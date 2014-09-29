package no.utgdev.sparkly.proxies;

import java.util.ArrayList;
import java.util.List;

public class ProxyChain {
    private List<ProxySetup> handlers = new ArrayList<>();

    public static ProxyChain start() {
        return new ProxyChain();
    }

    public ProxyChain with(ProxySetup handler) {
        this.handlers.add(handler);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T build(T instance, Class<T> type) {
        try {
            T proxy = instance;
            for (ProxySetup ps : handlers) {
                T obj = ps.pf.create(proxy, type);
                if (ps.pc != null) {
                    ps.pc.configure(obj);
                }
                proxy = type.cast(obj);
            }
            return proxy;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
