package pl.supereasy.sectors.api.packets.impl.player;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class PlayerToPlayerTeleportPacket extends Packet {

    private UUID playerTeleport;
    private UUID targetPlayer;

    public PlayerToPlayerTeleportPacket() {
    }

    public PlayerToPlayerTeleportPacket(UUID playerTeleport, UUID targetPlayer) {
        this.playerTeleport = playerTeleport;
        this.targetPlayer = targetPlayer;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getPlayerTeleport() {
        return playerTeleport;
    }

    public UUID getTargetPlayer() {
        return targetPlayer;
    }
}
