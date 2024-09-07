package pl.supereasy.sectors.api.packets.impl.player;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class PlayerSpawnTeleportPacket extends Packet {

    private UUID uuid;

    public PlayerSpawnTeleportPacket() {
    }

    public PlayerSpawnTeleportPacket(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getUUID() {
        return uuid;
    }
}
