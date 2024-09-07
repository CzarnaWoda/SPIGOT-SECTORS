package pl.supereasy.sectors.proxies;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ProxyManager {

    private static final Map<String, Proxy> proxies = new HashMap<>();


    public static void registerProxy(final Proxy proxy) {
        proxies.put(proxy.getProxyName(), proxy);
    }

    public static Proxy getProxy(final String name) {
        return proxies.get(name);
    }

    public static Collection<Proxy> getProxies() {
        return proxies.values();
    }
}
