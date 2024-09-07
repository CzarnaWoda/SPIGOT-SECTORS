package pl.supereasy.sectors.guilds.log.impl;

import pl.supereasy.sectors.guilds.log.api.GuildLoggerManager;
import pl.supereasy.sectors.guilds.log.api.LoggerEvent;
import pl.supereasy.sectors.guilds.log.enums.GuildLogType;

public class GuildLoggerFactory {

    private GuildLoggerManager loggerManager;

    private GuildLoggerFactory() {

    }

    public static GuildLoggerFactory getFactory() {
        return new GuildLoggerFactory();
    }

    public GuildLoggerFactory setupLogger() {
        this.loggerManager = new GuildLoggerManagerImpl();
        return this;
    }

    public GuildLoggerFactory attachObserver(final GuildLogType logType, final LoggerEvent event) {
        this.loggerManager.subscribe(logType, event);
        return this;
    }

    public GuildLoggerManager asManager() {
        return this.loggerManager;
    }
}
