package no.utgdev.page;

import no.utgdev.sparkly.proxies.ProxyConfiguration;
import org.mockito.internal.util.MockUtil;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class TestDAOMock implements ProxyConfiguration {

    @Override
    public void configure(Object proxy) {
        if (new MockUtil().isMock(proxy) && proxy instanceof Route) {
            Route r = (Route) proxy;
            List<String> rand = asList("Hei", "Hallo", "Bonjour", "Guten Tag");
            when(r.handle(any(Request.class), any(Response.class))).thenAnswer(invocation -> rand.get((int) (Math.random() * rand.size())));
        }
    }
}

