package pl.supereasy.sectors.api.packets.impl.proxies;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class ProxyStatusPacket extends Packet {

    private String proxyName;
    private double cpuUsage;

    public ProxyStatusPacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }


    public double getCpuUsage() {
        return cpuUsage;
    }

    public String getProxyName() {
        return proxyName;
    }

}
