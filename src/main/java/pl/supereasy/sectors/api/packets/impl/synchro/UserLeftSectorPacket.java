package pl.supereasy.sectors.api.packets.impl.synchro;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class UserLeftSectorPacket extends Packet {

    private String userName;
    private String sectorName;

    public UserLeftSectorPacket(String userName, String sectorName) {
        this.userName = userName;
        this.sectorName = sectorName;
    }

    public UserLeftSectorPacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getUserName() {
        return userName;
    }

    public String getSectorName() {
        return sectorName;
    }
}

