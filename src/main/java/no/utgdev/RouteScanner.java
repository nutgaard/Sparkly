package no.utgdev;

import no.utgdev.annotations.Delete;
import no.utgdev.annotations.Get;
import no.utgdev.annotations.Page;
import no.utgdev.annotations.Post;
import no.utgdev.annotations.Put;
import org.reflections.Reflections;
import org.slf4j.Logger;
import spark.Route;
import spark.Spark;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.slf4j.LoggerFactory.getLogger;

public class RouteScanner {
    final static Logger logger = getLogger(RouteScanner.class);
    private final Reflections reflections;

    public RouteScanner(String scanPackage) {
        logger.debug("Scanning package: "+scanPackage);
        this.reflections = new Reflections(scanPackage);
    }

    public void createRoutes() {
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Page.class);
        logger.debug("Found "+annotatedClasses.size()+" classes matching Page.class");
        logger.debug("  "+ Arrays.toString(annotatedClasses.toArray()));
        annotatedClasses.forEach(aClass -> {
            final String uri = aClass.getAnnotation(Page.class).value();
            try {
                final Object instance = aClass.getConstructors()[0].newInstance();

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
                .map((m) -> new Object[]{
                        m,
                        (Route) (request, response) -> {
                            try {
                                return m.invoke(instance, request, response);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return "";
                        }})
                .map((mp) -> {
                    Method m = (Method) mp[0];
                    Route route = (Route) mp[1];
                    ProxyChain pc = ProxyChainUtils.createProxyChainFromAnnotations(instance.getClass(), m);
                    return pc.build(route, Route.class);
                });
    }

}
