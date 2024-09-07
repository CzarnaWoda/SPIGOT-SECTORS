package pl.supereasy.sectors.proxies;

import org.redisson.api.RSet;
import pl.supereasy.sectors.SectorPlugin;

public class Proxy {

    private final String proxyName;
    private double cpuUsage;
    private Long lastUpdate;
    private final RSet<String> redisOnlinePlayers;

    public Proxy(String proxyName) {
        this.proxyName = proxyName;
        this.lastUpdate = (long) -1;
        this.redisOnlinePlayers = SectorPlugin.getInstance().getRedissonClient().getSet(proxyName + "-online");
    }


    public String getProxyName() {
        return proxyName;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public RSet<String> getOnlinePlayers() {
        return redisOnlinePlayers;
    }

    public void tickUpdate() {
        this.lastUpdate = System.currentTimeMillis();
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }
}
