package pl.supereasy.sectors.api.packets.impl.player;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class PlayerRandomTeleportPacket extends Packet {

    private UUID uuid;

    public PlayerRandomTeleportPacket(UUID uuid) {
        this.uuid = uuid;
    }

    public PlayerRandomTeleportPacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getUUID() {
        return uuid;
    }
}
