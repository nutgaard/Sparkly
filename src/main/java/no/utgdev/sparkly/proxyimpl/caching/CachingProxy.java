package no.utgdev.sparkly.proxyimpl.caching;

import no.utgdev.sparkly.proxies.ProxyFactory;
import spark.Route;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static java.util.Arrays.asList;

public class CachingProxy implements ProxyFactory {

    @SuppressWarnings("unchecked")
    public <T> T create(T instance, Class<T> type) {
        return type.cast(Proxy.newProxyInstance(
                CachingProxy.class.getClassLoader(),
                new Class[]{Route.class},
                new CacherProxy(instance)
        ));
    }

    static class CacherProxy implements InvocationHandler {
        private Object instance;
        private final Cache cache;

        CacherProxy(Object instance) {
            this.instance = instance;
            this.cache = Cache.getInstance(instance.getClass().getName(), true);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            String argsString = createStringFromArgs(args);
            HashcodeEquals he = createHashcodeEqualsWrapper(methodName, argsString);

            Object o = cache.get(he);
            if (o != null) {
                return o;
            }else {
                o = method.invoke(instance, args);
                cache.put(he, o);
            }
            return o;
        }

        private HashcodeEquals createHashcodeEqualsWrapper(Object... args) {
            return new HashcodeEquals() {
                @Override
                public int hashCode() {
                    return asList(args)
                            .stream()
                            .map(Object::hashCode)
                            .reduce((a, b) -> a+b)
                            .get();
                }
            };
        }

        private static String createStringFromArgs(Object[] args) {
            StringBuilder sb = new StringBuilder();
            for (Object arg : args) {
                sb.append(arg.toString().split("@")[0]).append("::");
            }
            return sb.toString();
        }
    }
}
