package no.utgdev.page;

import no.utgdev.sparkly.annotations.Injectable;
import no.utgdev.sparkly.proxyimpl.logging.Logging;
import no.utgdev.sparkly.proxyimpl.mocking.Mocking;

import java.util.Date;

@Logging
@Mocking(configuringClass = TestDAOMock.class)
@Injectable
public class TestDAO {

    public TestDAO(Date needed) {
        System.out.println("HALLLAAA");
    }

    public String getResp() {
        return "Halla";
    }
}
