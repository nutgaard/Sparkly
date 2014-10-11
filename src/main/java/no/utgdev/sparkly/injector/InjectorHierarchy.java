package no.utgdev.sparkly.injector;

import no.utgdev.sparkly.annotations.Injectable;
import no.utgdev.sparkly.injector.exceptions.CouldNotFindInjectableException;
import no.utgdev.sparkly.injector.injectables.AbstractInjectable;
import no.utgdev.sparkly.injector.injectables.InjectableFromMethod;
import no.utgdev.sparkly.injector.injectables.InjectableFromType;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class InjectorHierarchy {
    final static Logger logger = getLogger(InjectorHierarchy.class);
    public static final Function<Method, AbstractInjectable> INJECTABLE_FROM_METHOD_FUNCTION = InjectableFromMethod::new;
    public static final Function<Class<?>, AbstractInjectable> INJECTABLE_FROM_TYPE_FUNCTION = InjectableFromType::new;

    private static InjectorHierarchy instance = new InjectorHierarchy();
    private Map<Class<?>, Object> objectMap = new HashMap<>();

    public static InjectorHierarchy getInstance() {
        return instance;
    }

    private InjectorHierarchy() {
    }

    public void setup(String scanPackage) {
        findAndInitializeInjectables(scanPackage);
        findAndInitializeInjectInstances(scanPackage);
    }

    public void setup() {
        setup("");
    }

    public Optional<Object> getInjectable(Class<?> ai) {
        return Optional.ofNullable(objectMap.get(ai));
    }

    private void findAndInitializeInjectInstances(String scanPackage) {
        Reflections reflections = new Reflections(scanPackage, new FieldAnnotationsScanner(), new MethodAnnotationsScanner());

        Set<Field> fields = reflections.getFieldsAnnotatedWith(Inject.class);
        Set<Constructor> constructors = reflections.getConstructorsAnnotatedWith(Inject.class);

        verifyPresenceOfFieldTypes(fields);
        verifyPresenceOfConstructorParameters(constructors);
    }

    private void verifyPresenceOfFieldTypes(Set<Field> fields) {
        for (Field field : fields) {
            if (!getInjectable(field.getType()).isPresent()) {
                throw new CouldNotFindInjectableException(field.getType());
            }
        }
        logger.debug("All injects for type Field have a binding.");
    }

    private void verifyPresenceOfConstructorParameters(Set<Constructor> constructors) {
        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            for (Class cls : parameterTypes) {
                if (!getInjectable(cls).isPresent()) {
                    throw new CouldNotFindInjectableException(constructor.getDeclaringClass(), cls);
                }
            }
        }
        logger.debug("All injects for type Constructor have a binding.");
    }

    private void findAndInitializeInjectables(String scanPackage) {
        Reflections reflections = new Reflections(scanPackage, new MethodAnnotationsScanner(), new TypeAnnotationsScanner());

        List<AbstractInjectable> injectables = new ArrayList<>();
        List<AbstractInjectable> methods = createInjectable(reflections.getMethodsAnnotatedWith(Injectable.class), INJECTABLE_FROM_METHOD_FUNCTION);
        List<AbstractInjectable> types = createInjectable(reflections.getTypesAnnotatedWith(Injectable.class), INJECTABLE_FROM_TYPE_FUNCTION);
        logger.debug("Scanning for providers complete, found " + types.size() + " classes and " + methods.size() + " methods.");

        Set<AbstractInjectable> declaringClasses = methods
                .stream()
                .map(AbstractInjectable::getDeclaringClass)
                .map(INJECTABLE_FROM_TYPE_FUNCTION)
                .collect(Collectors.toSet());

        logger.debug("Adding " + declaringClasses.size() + " declaring classes.");
        injectables.addAll(types);
        injectables.addAll(methods);
        injectables.addAll(declaringClasses);


        logger.debug("Initializing provider objects...");
        resolve(injectables);
        logger.debug("Initialization of provider objects complete.");
    }

    private <T> List<AbstractInjectable> createInjectable(Set<T> types, Function<T, AbstractInjectable> mapper) {
        return types.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    private void resolve(List<AbstractInjectable> injectables) {
        List<AbstractInjectable> success = new ArrayList<>();
        List<AbstractInjectable> classes = new ArrayList<>(injectables);
        int prevMapSize;

        do {
            prevMapSize = objectMap.size();
            success.clear();
            for (AbstractInjectable injectable : classes) {
                try {
                    Object obj = injectable.initialize();
                    objectMap.put(injectable.getReturningClass(), obj);
                    success.add(injectable);
                } catch (Exception ignored) {

                }
            }
            success.forEach(classes::remove);
        } while (objectMap.size() > prevMapSize);

        if (objectMap.size() != injectables.size()) {
            throw new RuntimeException("Could not initialize all injectables");
        }
        logger.debug("All injectables found and places in objectMap");
    }
}
