package pl.supereasy.sectors.guilds.log.api;

import pl.supereasy.sectors.guilds.log.enums.GuildLogType;

public interface GuildLoggerManager {

    void subscribe(final GuildLogType type, LoggerEvent event);

    void unsubscribe(final GuildLogType logType);

    LoggerEvent getLogger(final GuildLogType logType);
}
