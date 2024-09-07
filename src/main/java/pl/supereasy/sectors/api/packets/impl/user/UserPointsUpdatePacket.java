package pl.supereasy.sectors.api.packets.impl.user;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class UserPointsUpdatePacket extends Packet {

    private UUID uuid;
    private int userPoints;

    public UserPointsUpdatePacket() {
    }

    public UserPointsUpdatePacket(UUID uuid, int userPoints) {
        this.uuid = uuid;
        this.userPoints = userPoints;
    }


    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getUUID() {
        return uuid;
    }

    public int getUserPoints() {
        return userPoints;
    }
}
