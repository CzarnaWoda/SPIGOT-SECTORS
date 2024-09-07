package pl.supereasy.sectors.guilds.log.impl;

import pl.supereasy.sectors.guilds.log.api.GuildLogAction;
import pl.supereasy.sectors.guilds.log.enums.GuildLogType;

import java.util.List;

public class GuildLogActionImpl implements GuildLogAction {


    private final String guildTag;
    private final String playerName;
    private final List<String> logDescription;
    private final Long logTime;
    private final GuildLogType logType;

    public GuildLogActionImpl(String guildTag, String playerName, List<String> logDescription, Long logTime, GuildLogType logType) {
        this.guildTag = guildTag;
        this.playerName = playerName;
        this.logDescription = logDescription;
        this.logTime = logTime;
        this.logType = logType;
    }

    @Override
    public String getGuildTag() {
        return this.guildTag;
    }

    @Override
    public String getPlayerName() {
        return this.playerName;
    }

    @Override
    public GuildLogType getLogType() {
        return this.logType;
    }

    @Override
    public List<String> getLogDescription() {
        return this.logDescription;
    }

    @Override
    public Long getLogTime() {
        return this.logTime;
    }
}
