package pl.supereasy.sectors.guilds.wars.api;

import pl.supereasy.sectors.api.redis.api.RedisData;

public interface War extends RedisData {

    GuildWar getFirstGuild();

    GuildWar getSecondGuild();

    default boolean hasStarted() {
        return System.currentTimeMillis() > getStartedTime();
    }

    Long getEndTime();

    Long getStartedTime();

    Long getCreatedTime();

}
