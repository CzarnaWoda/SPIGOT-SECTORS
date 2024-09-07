package pl.supereasy.sectors.api.packets.impl.user;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class UserLogoutPacket extends Packet {

    private UUID uuid;
    private String newSector;

    public UserLogoutPacket(UUID uuid, String newSector) {
        this.uuid = uuid;
        this.newSector = newSector;
    }

    public UserLogoutPacket() {
    }


    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getNewSector() {
        return newSector;
    }
}
