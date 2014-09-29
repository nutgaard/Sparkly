package no.utgdev.sparkly.proxies;

public class ProxySetup {
    final ProxyFactory pf;
    final ProxyConfiguration pc;

    ProxySetup(ProxyFactory pf, ProxyConfiguration pc) {
        this.pf = pf;
        this.pc = pc;
    }
}
