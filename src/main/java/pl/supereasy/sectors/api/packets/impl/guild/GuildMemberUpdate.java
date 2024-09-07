package pl.supereasy.sectors.api.packets.impl.guild;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;


public class GuildMemberUpdate extends Packet {

    private String guildTag;
    private UUID memberUUID;
    private String rowName;
    private int value;
    private String executor;

    public GuildMemberUpdate() {
    }

    public GuildMemberUpdate(String guildTag, UUID memberUUID, String rowName, int value, String executor) {
        this.guildTag = guildTag;
        this.memberUUID = memberUUID;
        this.rowName = rowName;
        this.value = value;
        this.executor = executor;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getGuildTag() {
        return guildTag;
    }

    public UUID getMemberUUID() {
        return memberUUID;
    }

    public String getRowName() {
        return rowName;
    }

    public int getValue() {
        return value;
    }

    public String getExecutor() {
        return executor;
    }
}

