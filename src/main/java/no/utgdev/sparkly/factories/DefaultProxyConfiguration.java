package no.utgdev.sparkly.factories;

import no.utgdev.sparkly.proxies.ProxyConfiguration;

public class DefaultProxyConfiguration implements ProxyConfiguration {

    @Override
    public void configure(Object proxy) {
        System.out.println("############ CONFIGURE");
    }
}
