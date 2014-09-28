package no.utgdev.page;

import no.utgdev.annotations.Get;
import no.utgdev.annotations.Logging;
import no.utgdev.annotations.Page;
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