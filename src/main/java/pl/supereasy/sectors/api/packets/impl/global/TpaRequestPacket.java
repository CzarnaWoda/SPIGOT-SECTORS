package pl.supereasy.sectors.api.packets.impl.global;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class TpaRequestPacket extends Packet {

    private UUID targetPlayer;
    private UUID requestPlayer;

    public TpaRequestPacket() {
    }

    public TpaRequestPacket(UUID targetPlayer, UUID requestPlayer) {
        this.targetPlayer = targetPlayer;
        this.requestPlayer = requestPlayer;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getTargetPlayer() {
        return targetPlayer;
    }

    public UUID getRequestPlayer() {
        return requestPlayer;
    }
}
