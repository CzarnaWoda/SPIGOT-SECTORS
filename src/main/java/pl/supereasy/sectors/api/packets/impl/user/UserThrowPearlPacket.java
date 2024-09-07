package pl.supereasy.sectors.api.packets.impl.user;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class UserThrowPearlPacket extends Packet {

    private UUID playerUUID;

    public UserThrowPearlPacket(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public UserThrowPearlPacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }
}
