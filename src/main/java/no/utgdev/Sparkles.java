package no.utgdev;

import no.utgdev.sparkly.Sparkly;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class Sparkles {
    final static Logger logger = getLogger(Sparkles.class);


    public static void main(String[] args) {
        String scanPackage = "no.utgdev.page";
        logger.info("Starting route scanner...");
        long start = System.nanoTime();
        Sparkly.start(scanPackage);
        logger.info("Route scanner completed in " + (System.nanoTime() - start) + "ns");
    }
}
