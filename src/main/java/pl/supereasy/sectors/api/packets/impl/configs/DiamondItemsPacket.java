package pl.supereasy.sectors.api.packets.impl.configs;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class DiamondItemsPacket extends Packet {

    private long time;
    public DiamondItemsPacket(long time){
        this.time = time;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public long getTime() {
        return time;
    }
}
