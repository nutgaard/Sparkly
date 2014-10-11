package no.utgdev.sparkly.injector.injectables;

import no.utgdev.sparkly.injector.InjectorHierarchy;
import no.utgdev.sparkly.injector.exceptions.CouldNotInitializeException;

import java.util.Optional;

public abstract class AbstractInjectable {
    public abstract Class<?> getDeclaringClass();
    public abstract Class<?> getReturningClass();

    public abstract Object initialize();

    protected static Object[] createParamInstances(Class[] paramsCls) {
        Object[] params = new Object[paramsCls.length];
        InjectorHierarchy om = InjectorHierarchy.getInstance();

        for (int i = 0; i < params.length; i++) {
            Optional<Object> injectable = om.getInjectable(paramsCls[i]);
            if (injectable.isPresent()) {
                params[i] = injectable.get();
            } else {
                throw new CouldNotInitializeException(paramsCls[i]);
            }
        }
        return params;
    }
}
