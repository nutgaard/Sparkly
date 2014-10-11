package no.utgdev.sparkly;

import no.utgdev.sparkly.annotations.wsrs.*;
import no.utgdev.sparkly.injector.InjectorHierarchy;
import org.reflections.Reflections;
import org.slf4j.Logger;
import spark.Route;
import spark.Spark;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.slf4j.LoggerFactory.getLogger;

public class Sparkly {
    final static Logger logger = getLogger(Sparkly.class);

    public static Sparkly start(String scanPackage) {
        return new Sparkly(scanPackage);
    }

    public Sparkly(String scanPackage) {
        logger.debug("Scanning package: " + scanPackage);
        this.startInjector(scanPackage);
        this.createRoutes(scanPackage);
    }

    private void startInjector(String scanPackage) {
        InjectorHierarchy.getInstance().setup(scanPackage);
    }

    private void createRoutes(String scanPackage) {
        Reflections reflections = new Reflections(scanPackage);
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Page.class);
        logger.debug("Found " + annotatedClasses.size() + " classes matching Page.class");
        logger.debug("  " + Arrays.toString(annotatedClasses.toArray()));
        annotatedClasses.forEach(aClass -> {
            final String uri = aClass.getAnnotation(Page.class).value();
            final InjectorHierarchy ih = InjectorHierarchy.getInstance();
            try {
                final Object instance = ih.getInjectable(aClass).get();

                createRoutesByType(instance, Get.class).forEach((r) -> Spark.get(uri, r));
                createRoutesByType(instance, Post.class).forEach((r) -> Spark.post(uri, r));
                createRoutesByType(instance, Delete.class).forEach((r) -> Spark.delete(uri, r));
                createRoutesByType(instance, Put.class).forEach((r) -> Spark.put(uri, r));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private Stream<Route> createRoutesByType(Object instance, Class<? extends Annotation> annotation) {
        return asList(instance.getClass().getMethods())
                .stream()
                .filter((m) -> m.isAnnotationPresent(annotation))
                .map((m) ->
                        (request, response) -> {
                            try {
                                return m.invoke(instance, request, response);
                            } catch (Exception e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                        });
    }

}
