package pl.supereasy.sectors.api.packets.impl.conn;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class SectorDisablePacket extends Packet {

    private String sectorName;

    public SectorDisablePacket(String sectorName) {
        this.sectorName = sectorName;
    }

    public SectorDisablePacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getSectorName() {
        return sectorName;
    }
}
