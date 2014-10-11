package no.utgdev.sparkly.injector.injectables;

import no.utgdev.sparkly.injector.InjectorHierarchy;
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
    public Object initialize() {
        InjectorHierarchy om = InjectorHierarchy.getInstance();
        Class<?>[] paramsCls = method.getParameterTypes();
        Object[] params = createParamInstances(paramsCls);

        Optional<Object> declaringClass = om.getInjectable(cls);
        if (!declaringClass.isPresent()) {
            throw new CouldNotInitializeException("Could not find any declaring class for " + method);
        }

        try {
            return method.invoke(declaringClass.get(), params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new CouldNotInitializeException("Invokation of method failed: " + method + "\n\r" + Arrays.toString(params));
        }
    }
}
