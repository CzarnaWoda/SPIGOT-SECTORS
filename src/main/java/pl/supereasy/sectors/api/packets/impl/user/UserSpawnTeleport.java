package pl.supereasy.sectors.api.packets.impl.user;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class UserSpawnTeleport extends Packet {

    private String userName;
    private String sectorName;

    public UserSpawnTeleport(String userName, String sectorName) {
        this.userName = userName;
        this.sectorName = sectorName;
    }

    public UserSpawnTeleport() {
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