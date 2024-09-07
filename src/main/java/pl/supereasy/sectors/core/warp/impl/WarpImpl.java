package pl.supereasy.sectors.core.warp.impl;

import org.bukkit.Location;
import pl.supereasy.sectors.api.redis.api.RedisData;
import pl.supereasy.sectors.api.redis.channels.RedisChannel;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.core.warp.api.Warp;
import pl.supereasy.sectors.util.GsonUtil;

public class WarpImpl implements Warp {

    private final String warpName;
    private final Location location;
    private final Sector sector;

    public WarpImpl(String warpName, Location location, Sector sector) {
        this.warpName = warpName;
        this.location = location;
        this.sector = sector;
    }

    @Override
    public Sector getSector() {
        return this.sector;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public String getName() {
        return this.warpName;
    }

    @Override
    public void insert(boolean async) {
        if (async) {
            RedisChannel.INSTANCE.WARPS.putAsync(this.warpName, GsonUtil.toJson(this));
        } else {
            RedisChannel.INSTANCE.WARPS.put(this.warpName, GsonUtil.toJson(this));
        }
    }

    @Override
    public void delete(boolean async) {
        if (async) {
            RedisChannel.INSTANCE.WARPS.removeAsync(this.warpName);
        } else {
            RedisChannel.INSTANCE.WARPS.remove(this.warpName);
        }
    }
}
