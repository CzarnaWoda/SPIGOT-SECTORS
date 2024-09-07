package pl.supereasy.sectors.api.packets.impl.global;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class TpaAcceptPacket extends Packet {

    private UUID targetPlayer;
    private String teleportPlayer;

    public TpaAcceptPacket() {
    }

    public TpaAcceptPacket(UUID targetPlayer, String teleportPlayer) {
        this.targetPlayer = targetPlayer;
        this.teleportPlayer = teleportPlayer;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getTargetPlayer() {
        return targetPlayer;
    }

    public String getTeleportPlayer() {
        return teleportPlayer;
    }
}
