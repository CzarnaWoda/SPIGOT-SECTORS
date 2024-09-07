package pl.supereasy.sectors.api.packets.impl.global;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class DeleteWarpPacket extends Packet {

    private String warpName;

    public DeleteWarpPacket() {
    }

    public DeleteWarpPacket(String warpName) {
        this.warpName = warpName;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getWarpName() {
        return warpName;
    }
}
