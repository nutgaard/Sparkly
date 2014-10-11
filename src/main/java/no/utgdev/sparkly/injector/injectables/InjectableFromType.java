package no.utgdev.sparkly.injector.injectables;

import no.utgdev.sparkly.injector.InjectorHierarchy;
import no.utgdev.sparkly.injector.exceptions.CouldNotInitializeException;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class InjectableFromType extends AbstractInjectable {
    private Class<?> cls;
    private List<ClassFieldPair> neededInjectables = new ArrayList<>();

    public InjectableFromType(Class<?> cls) {
        super();
        this.cls = cls;
        findNeededInjectables();
    }

    private void findNeededInjectables() {
        neededInjectables = asList(this.cls.getDeclaredFields()).stream()
                .filter((f) -> f.isAnnotationPresent(Inject.class))
                .map((f) -> new ClassFieldPair(f.getType(), f))
                .collect(Collectors.toList());
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
        verifyPresenceOfInjectables();
        Object obj = initializeObject();
        injectInjectables(obj);
        return obj;
    }

    private void injectInjectables(Object obj) {
        try {
            InjectorHierarchy ih = InjectorHierarchy.getInstance();
            for (ClassFieldPair needInjectable : neededInjectables) {
                String fieldName = needInjectable.field.getName();
                Field field = obj.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);

                Object value = ih.getInjectable(needInjectable.cls).get();

                field.set(obj, value);
            }
        } catch (NoSuchFieldException e) {
            throw new CouldNotInitializeException("Could not find field: ",e);
        } catch (IllegalAccessException e) {
            throw new CouldNotInitializeException("Could not set value to field: ", e);
        } catch (IllegalArgumentException e) {
            throw new CouldNotInitializeException("Illegal argument found: ", e);
        }
    }

    private void verifyPresenceOfInjectables() {
        InjectorHierarchy ih = InjectorHierarchy.getInstance();
        for (ClassFieldPair neededInjectable : neededInjectables) {
            if (!ih.getInjectable(neededInjectable.cls).isPresent()) {
                throw new CouldNotInitializeException("Undefined injectables, should have failed before this point...");
            }
        }
    }

    private Object initializeObject() {
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
        for (Constructor constructor : constructors) {
            Object obj = isSupportedInstance(constructor);
            if (obj != null) {
                return obj;
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

    private class ClassFieldPair {
        public final Class<?> cls;
        public final Field field;

        private ClassFieldPair(Class<?> cls, Field field) {
            this.cls = cls;
            this.field = field;
        }
    }
}
