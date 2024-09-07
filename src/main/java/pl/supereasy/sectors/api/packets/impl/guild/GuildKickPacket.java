package pl.supereasy.sectors.api.packets.impl.guild;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class GuildKickPacket extends Packet {

    private String guildTag;
    private UUID kickedUUID;
    private String kickedName;
    private String executorName;

    public GuildKickPacket() {
    }

    public GuildKickPacket(String guildTag, UUID kickedUUID, String kickedName, String executorName) {
        this.guildTag = guildTag;
        this.kickedUUID = kickedUUID;
        this.kickedName = kickedName;
        this.executorName = executorName;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getGuildTag() {
        return guildTag;
    }

    public UUID getKickedUUID() {
        return kickedUUID;
    }

    public String getExecutorName() {
        return executorName;
    }

    public String getKickedName() {
        return kickedName;
    }
}
