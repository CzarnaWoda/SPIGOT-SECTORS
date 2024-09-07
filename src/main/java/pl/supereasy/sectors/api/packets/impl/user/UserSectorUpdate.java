package pl.supereasy.sectors.api.packets.impl.user;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class UserSectorUpdate extends Packet {

    private UUID uuid;
    private String sectorName;

    public UserSectorUpdate() {
    }

    public UserSectorUpdate(UUID uuid, String sectorName) {
        this.uuid = uuid;
        this.sectorName = sectorName;
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
}
