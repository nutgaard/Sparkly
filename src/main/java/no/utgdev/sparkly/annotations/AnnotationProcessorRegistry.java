package no.utgdev.sparkly.annotations;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationProcessorRegistry {
    private static AnnotationProcessorRegistry instance = new AnnotationProcessorRegistry();

    public static AnnotationProcessorRegistry getInstance() {
        return instance;
    }

    private AnnotationProcessorRegistry() {
        scanForProcessors();
    }

    private void scanForProcessors() {
        Reflections ref = new Reflections("");
        Set<Class<? extends AnnotationProcessor>> processors = ref.getSubTypesOf(AnnotationProcessor.class);
        processors.stream()
                .filter(AnnotationProcessor.class::isAssignableFrom)
                .forEach((p) -> {
                    try {
                        AnnotationProcessor ap = p.newInstance();
                        this.registry.put(ap.accepts(), ap);
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
        System.out.println();
    }

    private Map<Class<? extends Annotation>, AnnotationProcessor> registry = new HashMap<>();


    public AnnotationProcessor getForAnnotation(Class<? extends Annotation> key) {
        AnnotationProcessor processor = this.registry.get(key);
        if (processor == null) {
            return null;
        }
        return processor;
    }
}
