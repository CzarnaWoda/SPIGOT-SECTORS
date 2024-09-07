package pl.supereasy.sectors.api.packets.impl.player;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class ClearInventoryPacket extends Packet {

    private UUID targetUUID;
    private String executorName;

    public ClearInventoryPacket() {
    }

    public ClearInventoryPacket(UUID targetUUID, String executorName) {
        this.targetUUID = targetUUID;
        this.executorName = executorName;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getTargetUUID() {
        return targetUUID;
    }

    public String getExecutorName() {
        return executorName;
    }
}
