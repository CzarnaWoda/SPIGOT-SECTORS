package pl.supereasy.sectors.api.packets.impl.guild;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class GuildAddMemberPacket extends Packet {

    private String guildTag;
    private UUID guildMemberUUID;
    private String guildMemberName;

    public GuildAddMemberPacket() {
    }

    public GuildAddMemberPacket(String guildTag, UUID guildMemberUUID, String guildMemberName) {
        this.guildTag = guildTag;
        this.guildMemberUUID = guildMemberUUID;
        this.guildMemberName = guildMemberName;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getGuildTag() {
        return guildTag;
    }

    public UUID getGuildMemberUUID() {
        return guildMemberUUID;
    }

    public String getGuildMemberName() {
        return guildMemberName;
    }
}
