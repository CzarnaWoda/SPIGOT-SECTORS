package pl.supereasy.sectors.api.packets.impl.global;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.Set;
import java.util.UUID;

public class TpaAcceptAllPacket extends Packet {

    private UUID targetPlayer;
    private Set<String> teleportPlayers;

    public TpaAcceptAllPacket() {
    }

    public TpaAcceptAllPacket(UUID targetPlayer, final Set<String> teleportPlayers) {
        this.targetPlayer = targetPlayer;
        this.teleportPlayers = teleportPlayers;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getTargetPlayer() {
        return targetPlayer;
    }

    public Set<String> getTeleportPlayers() {
        return teleportPlayers;
    }
}