package no.utgdev.page;

import no.utgdev.sparkly.annotations.wsrs.Get;
import no.utgdev.sparkly.annotations.interceptors.Logging;
import no.utgdev.sparkly.annotations.wsrs.Page;
import spark.Request;
import spark.Response;

@Page("/hello")
@Logging
public class JavaPage {

    @Get
    public Object handle(Request request, Response response) {
        return "Hello world";
    }

}
