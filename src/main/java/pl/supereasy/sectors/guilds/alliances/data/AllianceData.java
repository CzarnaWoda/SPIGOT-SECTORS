package pl.supereasy.sectors.guilds.alliances.data;

import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.redis.channels.RedisChannel;
import pl.supereasy.sectors.api.sql.api.Loadable;
import pl.supereasy.sectors.guilds.alliances.api.Alliance;
import pl.supereasy.sectors.guilds.alliances.impl.AllianceImpl;
import pl.supereasy.sectors.guilds.wars.api.War;
import pl.supereasy.sectors.guilds.wars.impl.WarImpl;
import pl.supereasy.sectors.util.GsonUtil;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class AllianceData implements Loadable {


    private final SectorPlugin plugin;

    public AllianceData(SectorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadData() {
        final AtomicInteger ai = new AtomicInteger(0);
        RedisChannel.INSTANCE.ALLIANCES.forEach((uuid, s) -> {
            final Alliance alliance = GsonUtil.fromJson(s, AllianceImpl.class);
            plugin.getAllianceManager().registerAlliance(alliance);
            ai.getAndIncrement();
        });
        plugin.getLogger().log(Level.INFO, "Zaladowano " + plugin.getWarManager().getWars().size() + " sojuszy!");
    }
}
