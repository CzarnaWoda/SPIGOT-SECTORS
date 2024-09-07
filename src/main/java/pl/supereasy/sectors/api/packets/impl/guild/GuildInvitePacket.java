package pl.supereasy.sectors.api.packets.impl.guild;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class GuildInvitePacket extends Packet {

    private String guildTag;
    private UUID invitedUUID;
    private String executorName;

    public GuildInvitePacket() {
    }

    public GuildInvitePacket(String guildTag, UUID invitedUUID, String executorName) {
        this.guildTag = guildTag;
        this.invitedUUID = invitedUUID;
        this.executorName = executorName;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getGuildTag() {
        return guildTag;
    }

    public UUID getInvitedUUID() {
        return invitedUUID;
    }

    public String getExecutorName() {
        return executorName;
    }
}
