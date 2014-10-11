package no.utgdev.page;

import no.utgdev.sparkly.annotations.Injectable;

import java.util.Date;

public class ConfigClass {

    @Injectable
    public Date now() {
        return new Date();
    }
}
