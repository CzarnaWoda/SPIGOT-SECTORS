package pl.supereasy.sectors.guilds.alliances.api;

import pl.supereasy.sectors.api.redis.api.RedisData;
import pl.supereasy.sectors.guilds.Guild;

import java.util.UUID;

public interface Alliance extends RedisData {

    UUID getID();

    GuildAlliance getFirstGuild();

    GuildAlliance getSecondGuild();

    GuildAlliance findGuildAlliance(final String tag);

    Long getAllianceCreatedTime();

}
