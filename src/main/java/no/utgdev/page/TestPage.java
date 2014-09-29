package no.utgdev.page;

import no.utgdev.sparkly.annotations.wsrs.Get;
import no.utgdev.sparkly.annotations.wsrs.Page;
import no.utgdev.sparkly.proxies.ProxyConfiguration;
import no.utgdev.sparkly.proxyimpl.caching.Caching;
import no.utgdev.sparkly.proxyimpl.logging.Logging;
import no.utgdev.sparkly.proxyimpl.mocking.Mocking;
import org.mockito.internal.util.MockUtil;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@Page("/test")
@Logging
@Mocking(configuringClass = TestPage.TestPageMockConfig.class)
@Caching
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
                List<String> rand = asList("Hei", "Hallo", "Bonjour", "Guten Tag");
                when(r.handle(any(Request.class), any(Response.class))).thenAnswer(invocation -> rand.get((int) (Math.random() * rand.size())));
            }
        }
    }
}
