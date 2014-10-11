package no.utgdev.page;

import no.utgdev.sparkly.annotations.Injectable;
import no.utgdev.sparkly.proxyimpl.caching.Caching;
import no.utgdev.sparkly.proxyimpl.logging.Logging;
import no.utgdev.sparkly.proxyimpl.mocking.Mocking;

import java.util.Date;

@Logging
@Mocking(configuringClass = TestDAOMock.class)
@Caching
@Injectable
public class TestDAO {

    public TestDAO(Date needed) {
        System.out.println("HALLLAAA" + needed);
    }

    public String getResp() {
        return "Halla";
    }

    @Override
    public String toString() {
        return "This is TestDAO";
    }
}
