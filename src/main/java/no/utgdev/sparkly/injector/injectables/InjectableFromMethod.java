package no.utgdev.sparkly.injector.injectables;

import no.utgdev.sparkly.injector.InjectorHierarchy;
import no.utgdev.sparkly.injector.ProxyUnwrapper;
import no.utgdev.sparkly.injector.exceptions.CouldNotInitializeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public class InjectableFromMethod extends AbstractInjectable {
    private Class<?> cls;
    private Method method;

    public InjectableFromMethod(Method method) {
        super();
        this.cls = method.getDeclaringClass();
        this.method = method;
    }

    @Override
    public Class<?> getDeclaringClass() {
        return this.cls;
    }

    @Override
    public Class<?> getReturningClass() {
        return this.method.getReturnType();
    }

    @Override
    public ProxyUnwrapper initialize() {
        InjectorHierarchy om = InjectorHierarchy.getInstance();
        Class<?>[] paramsCls = method.getParameterTypes();
        Object[] params = createParamInstances(paramsCls);

        Optional<ProxyUnwrapper> declaringClass = om.getInjectable(cls);
        if (!declaringClass.isPresent()) {
            throw new CouldNotInitializeException("Could not find any declaring class for " + method);
        }

        try {
            Object object = method.invoke(declaringClass.get().proxy, params);
            return new ProxyUnwrapper(object, object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new CouldNotInitializeException("Invokation of method failed: " + method + "\n\r" + Arrays.toString(params));
        }
    }
}
