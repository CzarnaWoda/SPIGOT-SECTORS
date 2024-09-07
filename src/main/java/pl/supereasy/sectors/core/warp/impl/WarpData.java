package pl.supereasy.sectors.core.warp.impl;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.redis.channels.RedisChannel;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.api.sql.api.Loadable;
import pl.supereasy.sectors.core.home.api.Home;
import pl.supereasy.sectors.core.home.impl.HomeImpl;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.core.warp.api.Warp;
import pl.supereasy.sectors.util.GsonUtil;

import java.sql.ResultSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class WarpData implements Loadable {

    private final SectorPlugin plugin;

    public WarpData(SectorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadData() {
        RedisChannel.INSTANCE.WARPS.forEach((name, s) -> {
            Warp warp = GsonUtil.fromJson(s, WarpImpl.class);
            plugin.getWarpManager().addWarp(warp);
        });
        plugin.getLogger().info("Loaded " + plugin.getWarpManager().getWarps().size() + " warps");
    }
}
