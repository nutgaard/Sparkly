package no.utgdev.sparkly.proxyimpl.caching;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import no.utgdev.sparkly.proxies.ProxyFactory;

import java.lang.reflect.Method;

import static java.util.Arrays.asList;

public class CachingProxy implements ProxyFactory {

    @SuppressWarnings("unchecked")
    public <T> T create(T instance, Class<T> type, Class[] argsCls, Object[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(type);
        enhancer.setCallback(new CacherProxy(instance));
        return (T) enhancer.create(argsCls, args);
    }

    static class CacherProxy implements MethodInterceptor {
        private Object instance;
        private final Cache cache;

        CacherProxy(Object instance) {
            this.instance = instance;
            this.cache = Cache.getInstance(instance.getClass().getName(), true);
        }

        @Override
        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            String methodName = method.getName();
            String argsString = createStringFromArgs(args);
            HashcodeEquals he = createHashcodeEqualsWrapper(methodName, argsString);

            Object o = cache.get(he);
            if (o != null) {
                return o;
            } else {
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
                            .reduce((a, b) -> a + b)
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
