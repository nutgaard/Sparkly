package page;

import no.utgdev.sparkly.proxies.ProxyConfiguration;
import org.mockito.internal.util.MockUtil;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;

public class TestDAOMock implements ProxyConfiguration {
    private int counter = 0;

    @Override
    public void configure(Object proxy) {
        if (new MockUtil().isMock(proxy) && proxy instanceof TestDAO) {
            TestDAO r = (TestDAO) proxy;
            List<String> rand = asList("Hei", "Hallo", "Bonjour", "Guten Tag");
            when(r.getResp()).thenAnswer(invocation -> rand.get((int) (Math.random() * rand.size())) + (counter++));
        }
    }
}

