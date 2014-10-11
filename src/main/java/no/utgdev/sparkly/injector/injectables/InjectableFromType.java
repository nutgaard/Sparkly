package no.utgdev.sparkly.injector.injectables;

import no.utgdev.sparkly.injector.InjectorHierarchy;
import no.utgdev.sparkly.injector.exceptions.CouldNotInitializeException;

import java.lang.reflect.Constructor;

public class InjectableFromType extends AbstractInjectable {
    private Class<?> cls;

    public InjectableFromType(Class<?> cls) {
        super();
        this.cls = cls;
    }

    @Override
    public Class<?> getDeclaringClass() {
        return this.cls;
    }

    @Override
    public Class<?> getReturningClass() {
        return this.cls;
    }

    @Override
    public Object initialize() {
        try {
            return cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            Constructor<?>[] constructors = cls.getConstructors();

            Object valid = firstConstructorSupported(constructors);
            if (valid == null) {
                throw new CouldNotInitializeException(cls, e);
            } else {
                return valid;
            }
        }
    }

    private Object firstConstructorSupported(Constructor<?>[] constructors) {
        InjectorHierarchy om = InjectorHierarchy.getInstance();

        for (Constructor constructor : constructors) {
            if (isSupportedInstance(constructor) != null) {
                return constructor;
            }
        }
        return null;
    }

    private static Object isSupportedInstance(Constructor constructor) {
        try {
            Class[] parameterTypes = constructor.getParameterTypes();
            Object[] paramInstances = createParamInstances(parameterTypes);
            return constructor.newInstance(paramInstances);
        } catch (Throwable e) {
            return null;
        }
    }
}
