package pl.supereasy.sectors.api.packets.impl.user;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class UserEatKoxPacket extends Packet {

    private UUID playerUUID;

    public UserEatKoxPacket(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public UserEatKoxPacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }
}