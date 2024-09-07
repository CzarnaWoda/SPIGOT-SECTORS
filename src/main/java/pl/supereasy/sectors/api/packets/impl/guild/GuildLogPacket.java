package pl.supereasy.sectors.api.packets.impl.guild;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;
import pl.supereasy.sectors.guilds.log.enums.GuildLogType;

import java.util.List;

public class GuildLogPacket extends Packet {

    private String guildTag;
    private String playerName;
    private List<String> logDescription;
    private Long logTime;
    private GuildLogType logType;

    public GuildLogPacket() {
    }

    public GuildLogPacket(String guildTag, String playerName, List<String> logDescription, GuildLogType logType) {
        this.guildTag = guildTag;
        this.playerName = playerName;
        this.logDescription = logDescription;
        this.logType = logType;
        this.logTime = System.currentTimeMillis();
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getGuildTag() {
        return guildTag;
    }

    public String getPlayerName() {
        return playerName;
    }

    public List<String> getLogDescription() {
        return logDescription;
    }

    public Long getLogTime() {
        return logTime;
    }

    public GuildLogType getLogType() {
        return logType;
    }
}
