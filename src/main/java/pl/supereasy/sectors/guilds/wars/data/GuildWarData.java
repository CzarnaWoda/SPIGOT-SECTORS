package pl.supereasy.sectors.guilds.wars.data;

import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.redis.channels.RedisChannel;
import pl.supereasy.sectors.api.sql.api.Loadable;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.wars.api.War;
import pl.supereasy.sectors.guilds.wars.impl.WarImpl;
import pl.supereasy.sectors.util.GsonUtil;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class GuildWarData implements Loadable {

    private final SectorPlugin plugin;


    public GuildWarData(SectorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadData() {
        final AtomicInteger ai = new AtomicInteger(0);
        RedisChannel.INSTANCE.WARS.forEach((uuid, s) -> {
            final War war = GsonUtil.fromJson(s, WarImpl.class);
            plugin.getWarManager().registerWar(war);
            ai.getAndIncrement();
        });
        plugin.getLogger().log(Level.INFO, "Zaladowano " + plugin.getWarManager().getWars().size() + " wojen!");
    }
}
