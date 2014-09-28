package no.utgdev.page;

import no.utgdev.sparkly.annotations.wsrs.Get;
import no.utgdev.sparkly.annotations.wsrs.Page;
import spark.Request;
import spark.Response;

@Page("/test")
public class TestPage {

    @Get
    public Object handle(Request request, Response response) {
        return "Dette er da en test";
    }

}
