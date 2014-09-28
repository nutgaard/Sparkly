package no.utgdev.proxy;

import org.slf4j.Logger;
import spark.Route;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import static org.slf4j.LoggerFactory.getLogger;

public class LoggingProxy implements ProxyFactory {
    final static Logger logger = getLogger(LoggingProxy.class);

    @SuppressWarnings("unchecked")
    public <T> T create(T instance, Class<T> type) {
        return (T) Proxy.newProxyInstance(
                LoggingProxy.class.getClassLoader(),
                new Class[]{Route.class},
                new LoggerProxy(instance));
    }

    static class LoggerProxy implements InvocationHandler {
        private Object instance;

        LoggerProxy(Object instance) {
            this.instance = instance;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            logger.info(String.format("START %s::%s(%s)",
                            instance.getClass().getName(),
                            method.getName(),
                            Arrays.toString(args))
            );
            long s = System.nanoTime();
            Object out = method.invoke(instance, args);
            s = System.nanoTime() - s;
            logger.info(String.format("END %s::%s(%s) @ %s",
                            instance.getClass().getName(),
                            method.getName(),
                            Arrays.toString(args),
                            s + "ns")
            );
            return out;
        }
    }
}
