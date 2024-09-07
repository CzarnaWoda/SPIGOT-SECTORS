package pl.supereasy.sectors.api.packets.impl.user;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class UserSetRankPacket extends Packet {

    private UUID targetUUID;
    private String executorName;
    private String rankType;

    public UserSetRankPacket() {
    }

    public UserSetRankPacket(UUID targetUUID, String executorName, String rankType) {
        this.targetUUID = targetUUID;
        this.executorName = executorName;
        this.rankType = rankType;
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

    public String getRankType() {
        return rankType;
    }
}
