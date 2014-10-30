package page;

import no.utgdev.sparkly.annotations.wsrs.Get;
import no.utgdev.sparkly.proxyimpl.logging.Logging;
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
