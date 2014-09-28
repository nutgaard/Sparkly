package no.utgdev.sparkly.proxies;

import no.utgdev.sparkly.annotations.interceptors.ProxyAnnotation;
import no.utgdev.sparkly.factories.ProxyFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Map.Entry;
import static org.slf4j.LoggerFactory.getLogger;

public class ProxyChainUtils {
    final static Logger logger = getLogger(ProxyChainUtils.class);
    private static Map<Class<? extends Annotation>, ProxyFactory> annotations;

    public static ProxyChain createProxyChainFromAnnotations(Class aClass, Method m) {
        try {
            checkForAnnotations();
            List<Class<?extends Annotation>> present = findAnnotations(aClass, m);
            ProxyChain pc = ProxyChain.start();
            present.stream().forEach((a) -> pc.with(annotations.get(a)));
            return pc;
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return new ProxyChain();
    }

    private static List<Class<? extends Annotation>> findAnnotations(Class aClass, Method m) {
        List<Class<? extends Annotation>> present = new ArrayList<>();
        for (Entry<Class<? extends Annotation>, ProxyFactory> entry : annotations.entrySet()) {
            Class<? extends Annotation> annotation = entry.getKey();

            if (aClass.isAnnotationPresent(annotation)) {
                present.add(annotation);
            }
            if (m.isAnnotationPresent(annotation)) {
                present.add(annotation);
            }
        }
        return present;
    }

    @SuppressWarnings("unchecked")
    private static void checkForAnnotations() throws IllegalAccessException, InstantiationException {
        if (annotations == null) {
            String path = ProxyAnnotation.class.getPackage().getName();
            logger.debug("Search path " + path + " for proxy annotations.");
            Reflections reflection = new Reflections(path);
            Set<Class<?>> proxyAnnotations = reflection.getTypesAnnotatedWith(ProxyAnnotation.class);
            logger.debug("Found " + proxyAnnotations.size() + " proxy annotations.");
            logger.debug("  " + Arrays.toString(proxyAnnotations.toArray()));

            annotations = new HashMap<>();
            for (Class<?> annotationCls : proxyAnnotations) {
                if (annotationCls.isAnnotation()) {
                    Class<Annotation> annotation = (Class<Annotation>) annotationCls;
                    ProxyFactory pa = annotationCls.getAnnotation(ProxyAnnotation.class).implementingClass().newInstance();
                    annotations.put(annotation, pa);
                }
            }
        }
    }
}
