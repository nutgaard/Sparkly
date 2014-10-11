package no.utgdev.sparkly.injector.injectables;

import no.utgdev.sparkly.injector.InjectorHierarchy;
import no.utgdev.sparkly.injector.ProxyUnwrapper;
import no.utgdev.sparkly.injector.exceptions.CouldNotInitializeException;
import no.utgdev.sparkly.proxies.ProxyChain;
import no.utgdev.sparkly.proxies.ProxyChainUtils;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class InjectableFromType<T> extends AbstractInjectable {
    private Class<T> cls;
    private List<ClassFieldPair> neededInjectables = new ArrayList<>();

    public InjectableFromType(Class<T> cls) {
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
    public ProxyUnwrapper initialize() {
        verifyPresenceOfInjectables();
        return initializeObject();
    }

    private T createProxyLayers(T obj, ConstructorArgumentsTriplet construct) {
        ProxyChain pc = ProxyChainUtils.createProxyChainFromAnnotations(obj);
        return pc.build(obj, this.cls, construct.argsCls, construct.args);
    }

    private void injectInjectables(T obj) {
        try {
            InjectorHierarchy ih = InjectorHierarchy.getInstance();
            for (ClassFieldPair needInjectable : neededInjectables) {
                String fieldName = needInjectable.field.getName();
                Field field = obj.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);

                ProxyUnwrapper value = ih.getInjectable(needInjectable.cls).get();

                field.set(obj, value.proxy);
            }
        } catch (NoSuchFieldException e) {
            throw new CouldNotInitializeException("Could not find field: ", e);
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

    private ProxyUnwrapper initializeObject() {
        T obj;
        ConstructorArgumentsTriplet<T> construct;
        try {
            construct = new ConstructorArgumentsTriplet<>(cls.getConstructor(new Class[0]), new Class[0], new Object[0]);
        } catch (NoSuchMethodException e) {
            Constructor<?>[] constructors = cls.getConstructors();
            construct = firstConstructorSupported(constructors);

            if (construct == null) {
                throw new CouldNotInitializeException(cls, e);
            }
        }
        try {
            obj = cls.cast(construct.constructor.newInstance(construct.args));
            injectInjectables(obj);
            T proxy = createProxyLayers(obj, construct);
            return new ProxyUnwrapper(proxy, obj);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new CouldNotInitializeException(cls, e);
        }
    }

    private ConstructorArgumentsTriplet<T> firstConstructorSupported(Constructor<?>[] constructors) {
        for (Constructor constructor : constructors) {
            ConstructorArgumentsTriplet<T> obj = isSupportedInstance(constructor);
            if (obj != null) {
                return obj;
            }
        }
        return null;
    }

    private static <T> ConstructorArgumentsTriplet<T> isSupportedInstance(Constructor constructor) {
        try {
            Class[] parameterTypes = constructor.getParameterTypes();
            Object[] paramInstances = createParamInstances(parameterTypes);
            return new ConstructorArgumentsTriplet<>(constructor, parameterTypes, paramInstances);
        } catch (Throwable e) {
            return null;
        }
    }

    static class ClassFieldPair {
        public final Class<?> cls;
        public final Field field;

        private ClassFieldPair(Class<?> cls, Field field) {
            this.cls = cls;
            this.field = field;
        }
    }

    static class ConstructorArgumentsTriplet<T> {
        public final Constructor<T> constructor;
        public final Class[] argsCls;
        public final Object[] args;

        private ConstructorArgumentsTriplet(Constructor constructor, Class[] argsCls, Object[] args) {
            this.constructor = constructor;
            this.argsCls = argsCls;
            this.args = args;
        }
    }
}
