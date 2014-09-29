package no.utgdev.page;

import no.utgdev.sparkly.annotations.interceptors.Mocking;
import no.utgdev.sparkly.annotations.wsrs.Get;
import no.utgdev.sparkly.annotations.wsrs.Page;
import no.utgdev.sparkly.proxies.ProxyConfiguration;
import org.mockito.internal.util.MockUtil;
import spark.Request;
import spark.Response;
import spark.Route;

@Page("/test")
@Mocking(configuringClass = TestPage.TestPageMockConfig.class)
public class TestPage {

    @Get
    public Object handle(Request request, Response response) {
        return "Dette er da en test";
    }

    public static class TestPageMockConfig implements ProxyConfiguration {
        public TestPageMockConfig() {
        }

        @Override
        public void configure(Object proxy) {
            if (new MockUtil().isMock(proxy) && proxy instanceof Route) {
                Route r = (Route) proxy;
                System.out.println(r);
            }
        }
    }
}
