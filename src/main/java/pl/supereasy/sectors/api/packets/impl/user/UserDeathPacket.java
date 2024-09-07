package pl.supereasy.sectors.api.packets.impl.user;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class UserDeathPacket extends Packet {

    private UUID uuid;
    private String deathMessage;

    public UserDeathPacket() {
    }

    public UserDeathPacket(UUID uuid, String deathMessage) {
        this.uuid = uuid;
        this.deathMessage = deathMessage;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getDeathMessage() {
        return deathMessage;
    }
}
