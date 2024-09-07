package pl.supereasy.sectors.api.packets.impl.guild;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class GuildWarStartPacket extends Packet {

    private String guildTag;
    private String guildName;
    private String secondGuildTag;
    private String secondGuildName;

    public GuildWarStartPacket(String guildTag, String guildName, String secondGuildTag, String secondGuildName) {
        this.guildTag = guildTag;
        this.guildName = guildName;
        this.secondGuildTag = secondGuildTag;
        this.secondGuildName = secondGuildName;
    }

    public GuildWarStartPacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getGuildTag() {
        return guildTag;
    }

    public String getGuildName() {
        return guildName;
    }

    public String getSecondGuildTag() {
        return secondGuildTag;
    }

    public String getSecondGuildName() {
        return secondGuildName;
    }
}

