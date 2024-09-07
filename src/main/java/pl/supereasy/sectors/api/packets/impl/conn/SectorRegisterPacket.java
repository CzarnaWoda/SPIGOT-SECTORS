package pl.supereasy.sectors.api.packets.impl.conn;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class SectorRegisterPacket extends Packet {

    private String sectorName;

    public SectorRegisterPacket(String sectorName) {
        this.sectorName = sectorName;
    }

    public SectorRegisterPacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getSectorName() {
        return sectorName;
    }
}
