package pl.supereasy.sectors.api.packets.impl.guild;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class GuildRemoveMemberPacket extends Packet {

    private String guildTag;
    private UUID kickedUUID;
    private String kickedName;
    private String executorName;

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

    public String getKickedName() {
        return kickedName;
    }

    public String getExecutorName() {
        return executorName;
    }
}
