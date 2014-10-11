package no.utgdev.sparkly.proxyimpl.logging;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import no.utgdev.sparkly.proxies.ProxyFactory;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.slf4j.LoggerFactory.getLogger;

public class LoggingProxy implements ProxyFactory {
    final static Logger logger = getLogger(LoggingProxy.class);

    @SuppressWarnings("unchecked")
    public <T> T create(T instance, Class<T> type, Class[] argsCls, Object[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(type);
        enhancer.setCallback(new LoggerProxy(instance));
        return (T) enhancer.create(argsCls, args);
    }

    static class LoggerProxy implements MethodInterceptor {
        private Object instance;

        LoggerProxy(Object instance) {
            this.instance = instance;
        }

        @Override
        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
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
