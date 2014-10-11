package no.utgdev.sparkly.proxyimpl.caching;

import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class Cache<S extends HashcodeEquals, T> {
    final static Logger logger = getLogger(Cache.class);

    private static Map<String, Cache> cachemanager = new HashMap<>();

    public static Cache getInstance(String cachename, boolean createIfNotExists) {
        Cache cache = cachemanager.get(cachename);
        if (cache == null && createIfNotExists) {
            cache = new Cache();
            cachemanager.put(cachename, cache);
        }
        return cache;
    }

    public static Cache getInstance(String cachename) {
        return getInstance(cachename, false);
    }

    private Map<Integer,Element> nativecache = new HashMap<>();
    private long expirationTime = 1000;

    private Cache() {
    }

    public void put(S s, T t) {
        this.nativecache.put(s.hashCode(), new Element(s, t));
    }
    public T get(S s) {
        Element element = this.nativecache.get(s.hashCode());

        if (element == null) {
            return null;
        }

        long now = System.currentTimeMillis();
        if (now-element.last < expirationTime) {
            logger.debug("Updating last time: "+(now-element.last));
            element.last = now;
            return element.t;
        }else {
            logger.debug("Evicting...");
            evict(s);
            return null;
        }
    }
    public T getOrElse(S s, T t) {
        T inCache = get(s);
        if (inCache == null) {
            return t;
        }
        return inCache;
    }
    public void evict(S s) {
        this.nativecache.remove(s.hashCode(), this.nativecache.get(s.hashCode()));
    }
    public void clear() {
        this.nativecache.clear();
    }
    class Element {
        final S s;
        final T t;
        long last;

        Element(S s, T t) {
            this.s = s;
            this.t = t;
            last = System.currentTimeMillis();
        }
    }
}
