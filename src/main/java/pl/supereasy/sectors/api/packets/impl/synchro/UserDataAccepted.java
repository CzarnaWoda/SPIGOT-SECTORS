package pl.supereasy.sectors.api.packets.impl.synchro;


import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

//Packet sent when sector accepted user data come valid and we can send him to this sector
public class UserDataAccepted extends Packet {

    private final UUID uuid;

    public UserDataAccepted() {
        this.uuid = null;
    }

    public UserDataAccepted(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getUuid() {
        return uuid;
    }
}
