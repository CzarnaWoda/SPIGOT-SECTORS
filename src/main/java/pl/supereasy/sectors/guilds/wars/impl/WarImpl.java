package pl.supereasy.sectors.guilds.wars.impl;

import pl.supereasy.sectors.api.redis.channels.RedisChannel;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.wars.api.GuildWar;
import pl.supereasy.sectors.guilds.wars.api.War;
import pl.supereasy.sectors.util.GsonUtil;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class WarImpl implements War {

    private final UUID warID;
    private final GuildWar firstGuild;
    private final GuildWar secondGuild;
    private final Long startedTime;
    private final Long createdTime;
    private final Long endTime;


    public WarImpl(GuildWar firstGuild, GuildWar secondGuild) {
        this.warID = UUID.randomUUID();
        this.firstGuild = firstGuild;
        this.secondGuild = secondGuild;
        this.createdTime = System.currentTimeMillis();
        this.startedTime = this.createdTime + TimeUnit.HOURS.toMillis(12);
        this.endTime = this.startedTime + TimeUnit.HOURS.toMillis(48);
    }

    public WarImpl(final Guild g, final Guild g2) {
        this.warID = UUID.randomUUID();
        this.firstGuild = new GuildWarImpl(g.getTag(), g);
        this.secondGuild = new GuildWarImpl(g2.getTag(), g2);
        this.createdTime = System.currentTimeMillis();
        this.startedTime = this.createdTime + TimeUnit.HOURS.toMillis(12);
        this.endTime = this.startedTime + TimeUnit.HOURS.toMillis(48);
    }

    @Override
    public GuildWar getFirstGuild() {
        return this.firstGuild;
    }

    @Override
    public GuildWar getSecondGuild() {
        return this.secondGuild;
    }

    @Override
    public Long getEndTime() {
        return this.endTime;
    }

    @Override
    public Long getStartedTime() {
        return this.startedTime;
    }

    @Override
    public Long getCreatedTime() {
        return this.createdTime;
    }

    @Override
    public void insert(boolean async) {
        if (async) {
            RedisChannel.INSTANCE.WARS.putAsync(this.warID, GsonUtil.toJson(this));
        } else {
            RedisChannel.INSTANCE.WARS.put(this.warID, GsonUtil.toJson(this));
        }
    }

    @Override
    public void delete(boolean async) {
        if (async) {
            RedisChannel.INSTANCE.WARS.removeAsync(this.warID);
        } else {
            RedisChannel.INSTANCE.WARS.remove(this.warID);
        }
    }
}
