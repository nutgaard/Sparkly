package no.utgdev.page;

import no.utgdev.annotations.Get;
import no.utgdev.annotations.Page;
import spark.Request;
import spark.Response;

@Page("/test")
public class TestPage {

    @Get
    public Object handle(Request request, Response response) {
        return "Dette er da en test";
    }

}
