package pl.supereasy.sectors.api.packets.impl.status;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class ServerStatusPacket extends Packet {

    private String serverName;
    private double currentTPS;
    private long statusTime;


    public ServerStatusPacket(String serverName, double currentTPS, long statusTime) {
        this.serverName = serverName;
        this.currentTPS = currentTPS;
        this.statusTime = statusTime;
    }

    public ServerStatusPacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getServerName() {
        return serverName;
    }

    public double getCurrentTPS() {
        return currentTPS;
    }

    public long getStatusTime() {
        return statusTime;
    }
}
