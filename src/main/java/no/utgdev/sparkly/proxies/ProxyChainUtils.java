package no.utgdev.sparkly.proxies;

import no.utgdev.sparkly.annotations.AnnotationProcessor;
import no.utgdev.sparkly.annotations.AnnotationProcessorRegistry;
import no.utgdev.sparkly.annotations.ProxyAnnotation;
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
    private static Map<Class<? extends Annotation>, ProxyFactory> proxyFactoryMap;

    public static ProxyChain createProxyChainFromAnnotations(Object instance, Method m) {
        try {
            checkForAnnotations();
            List<ProxySetup> instanceAnnotations = findAnnotations(instance, m);
            ProxyChain pc = ProxyChain.start();
            instanceAnnotations.stream().forEach(pc::with);
            return pc;
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return new ProxyChain();
    }

    private static List<ProxySetup> findAnnotations(Object instance, Method m) {
        Class instanceClass = instance.getClass();
        List<ProxySetup> present = new ArrayList<>();
        AnnotationProcessorRegistry registry = AnnotationProcessorRegistry.getInstance();
        for (Entry<Class<? extends Annotation>, ProxyFactory> entry : proxyFactoryMap.entrySet()) {
            Class<? extends Annotation> proxyFactoryAnnotation = entry.getKey();


            if (instanceClass.isAnnotationPresent(proxyFactoryAnnotation)) {
                AnnotationProcessor processor = registry.getForAnnotation(proxyFactoryAnnotation);
                ProxyConfiguration configurator = null;
                if (processor != null && proxyFactoryAnnotation.equals(processor.accepts())) {
                    configurator = processor.process(instance.getClass().getAnnotation(proxyFactoryAnnotation));
                }
                present.add(new ProxySetup(proxyFactoryMap.get(proxyFactoryAnnotation), configurator));
            }
            if (m.isAnnotationPresent(proxyFactoryAnnotation)) {
                present.add(new ProxySetup(proxyFactoryMap.get(proxyFactoryAnnotation), null));
            }
        }
        return present;
    }

    @SuppressWarnings("unchecked")
    private static void checkForAnnotations() throws IllegalAccessException, InstantiationException {
        if (proxyFactoryMap == null) {
            String path = "";
            logger.debug("Search path " + path + " for proxy annotations.");
            Reflections reflection = new Reflections(path);
            Set<Class<?>> proxyAnnotations = reflection.getTypesAnnotatedWith(ProxyAnnotation.class);
            logger.debug("Found " + proxyAnnotations.size() + " proxy annotations.");
            logger.debug("  " + Arrays.toString(proxyAnnotations.toArray()));

            proxyFactoryMap = new HashMap<>();
            for (Class<?> annotationCls : proxyAnnotations) {
                if (annotationCls.isAnnotation()) {
                    Class<Annotation> annotation = (Class<Annotation>) annotationCls;
                    ProxyAnnotation pa = annotationCls.getAnnotation(ProxyAnnotation.class);
                    ProxyFactory pf = pa.implementingClass().newInstance();
                    proxyFactoryMap.put(annotation, pf);
                }
            }
        }
    }
}
