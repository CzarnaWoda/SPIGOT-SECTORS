package pl.supereasy.sectors.guilds.log.impl;

import pl.supereasy.sectors.guilds.log.api.GuildLoggerManager;
import pl.supereasy.sectors.guilds.log.api.LoggerEvent;
import pl.supereasy.sectors.guilds.log.enums.GuildLogType;

import java.util.HashMap;
import java.util.Map;

public class GuildLoggerManagerImpl implements GuildLoggerManager {

    private final Map<GuildLogType, LoggerEvent> loggerMap;

    public GuildLoggerManagerImpl() {
        this.loggerMap = new HashMap<>();
    }

    @Override
    public void subscribe(GuildLogType type, LoggerEvent event) {
        this.loggerMap.putIfAbsent(type, event);
    }

    @Override
    public void unsubscribe(GuildLogType logType) {
        this.loggerMap.remove(logType);
    }

    @Override
    public LoggerEvent getLogger(GuildLogType logType) {
        return this.loggerMap.get(logType);
    }
}
