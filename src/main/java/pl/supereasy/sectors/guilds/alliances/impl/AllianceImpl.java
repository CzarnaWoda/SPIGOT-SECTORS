package pl.supereasy.sectors.guilds.alliances.impl;

import pl.supereasy.sectors.api.redis.api.RedisData;
import pl.supereasy.sectors.api.redis.channels.RedisChannel;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.alliances.api.Alliance;
import pl.supereasy.sectors.guilds.alliances.api.GuildAlliance;
import pl.supereasy.sectors.guilds.enums.AlliancePermission;
import pl.supereasy.sectors.util.GsonUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AllianceImpl implements Alliance {

    private final UUID allianceID;
    private final GuildAllianceImpl firstGuild;
    private final GuildAllianceImpl secondGuild;
    private final Long allianceCreated;

    public AllianceImpl(final UUID allianceID, final Guild g, final Guild g2) {
        this.allianceID = allianceID;
        this.firstGuild = new GuildAllianceImpl(g.getTag());
        this.secondGuild = new GuildAllianceImpl(g2.getTag());
        this.allianceCreated = System.currentTimeMillis();
    }

    @Override
    public Long getAllianceCreatedTime() {
        return allianceCreated;
    }

    @Override
    public UUID getID() {
        return this.allianceID;
    }

    @Override
    public GuildAlliance getFirstGuild() {
        return firstGuild;
    }

    @Override
    public GuildAlliance getSecondGuild() {
        return secondGuild;
    }

    @Override
    public GuildAlliance findGuildAlliance(String tag) {
        if (this.firstGuild.getTag().equals(tag)) {
            return this.firstGuild;
        } else if (this.secondGuild.getTag().equals(tag)) {
            return this.secondGuild;
        }
        return null;
    }

    @Override
    public void insert(boolean async) {
        if (async) {
            RedisChannel.INSTANCE.ALLIANCES.putAsync(this.allianceID, GsonUtil.toJson(this));
        } else {
            RedisChannel.INSTANCE.ALLIANCES.put(this.allianceID, GsonUtil.toJson(this));
        }
    }

    @Override
    public void delete(boolean async) {
        if (async) {
            RedisChannel.INSTANCE.ALLIANCES.removeAsync(this.allianceID);
        } else {
            RedisChannel.INSTANCE.ALLIANCES.remove(this.allianceID);
        }
    }
}
