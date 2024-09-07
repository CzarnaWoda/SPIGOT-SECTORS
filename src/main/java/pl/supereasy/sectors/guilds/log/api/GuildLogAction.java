package pl.supereasy.sectors.guilds.log.api;

import pl.supereasy.sectors.guilds.log.enums.GuildLogType;

import java.util.List;

public interface GuildLogAction {

    String getGuildTag();

    String getPlayerName();

    GuildLogType getLogType();

    List<String> getLogDescription();

    Long getLogTime();

}
