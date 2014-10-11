package no.utgdev.page;

import no.utgdev.sparkly.annotations.wsrs.Get;
import no.utgdev.sparkly.annotations.wsrs.Page;
import spark.Request;
import spark.Response;

import javax.inject.Inject;

@Page("/test")
public class TestPage {

    @Inject
    private TestDAO dao;

    @Inject
    public TestPage() {

    }

    @Get
    public Object handle(Request request, Response response) {
        System.out.println("DAO:::"+dao);
        return dao.getResp();
    }
}
