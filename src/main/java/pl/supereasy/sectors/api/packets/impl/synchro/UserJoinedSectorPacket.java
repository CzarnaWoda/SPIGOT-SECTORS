package pl.supereasy.sectors.api.packets.impl.synchro;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class UserJoinedSectorPacket extends Packet {

    private String userName;
    private UUID uuid;
    private String sectorName;

    public UserJoinedSectorPacket(String userName, UUID uuid, String sectorName) {
        this.userName = userName;
        this.uuid = uuid;
        this.sectorName = sectorName;
    }

    public UserJoinedSectorPacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getSectorName() {
        return sectorName;
    }

    public String getUserName() {
        return userName;
    }
}


