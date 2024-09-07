package pl.supereasy.sectors.api.packets.impl.user;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class UserEatRefPacket extends Packet {

    private UUID playerUUID;

    public UserEatRefPacket(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public UserEatRefPacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }
}
