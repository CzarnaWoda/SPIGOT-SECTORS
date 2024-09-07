package pl.supereasy.sectors.api.packets.impl.global;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class DeleteHomePacket extends Packet {

    private UUID userUUID;
    private String homeName;

    public DeleteHomePacket() {
    }

    public DeleteHomePacket(UUID userUUID, String homeName) {
        this.userUUID = userUUID;
        this.homeName = homeName;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getUserUUID() {
        return userUUID;
    }

    public String getHomeName() {
        return homeName;
    }
}
