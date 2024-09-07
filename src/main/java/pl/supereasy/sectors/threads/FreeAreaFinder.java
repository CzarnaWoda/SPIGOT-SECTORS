package pl.supereasy.sectors.threads;

import io.vavr.collection.Set;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.threads.api.Schedulable;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.area.GuildArea;
import pl.supereasy.sectors.threads.api.ThreadExecutorAPI;
import pl.supereasy.sectors.util.RandomUtil;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class FreeAreaFinder extends Schedulable {

    private final int mindistance = GuildConfig.INSTANCE.VALUES_GUILD_REGION_MAXSIZE + GuildConfig.INSTANCE.VALUES_GUILD_REGION_DISTANCEBETWEEN; //80 - guildsizemax 15- distance between


    @Override
    public void start() {
        ThreadExecutorAPI.SCHEDULED.scheduleAtFixedRate(() -> {
            try {
                final Sector sector = SectorPlugin.getInstance().getSectorManager().getCurrentSector();
                if (sector.getGuildAreas().size() > 20) {
                    return;
                }
                for (int i = 0; i < 50; i++) {
                    int randomX = RandomUtil.getRandInt(sector.getMinX(), sector.getMaxX());
                    int randomZ = RandomUtil.getRandInt(sector.getMinZ(), sector.getMaxZ());
                    final Set<Guild> guilds = SectorPlugin.getInstance().getGuildManager().getGuilds();
                    for (Guild guild : guilds) {
                        if ((Math.abs(guild.getCenterX() - randomX) <= mindistance) && (Math.abs(guild.getCenterZ() - randomZ) <= mindistance)) {
                            return;
                        }
                    }
                    sector.getGuildAreas().add(new GuildArea(sector.getSectorName(), randomX, randomZ));
                    SectorPlugin.getInstance().getLogger().log(Level.INFO, "Znaleziono wolne miejsce na sektorze " + sector.getSectorName() + " koordy: " + randomX + " oraz " + randomZ);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 5, 5, TimeUnit.SECONDS);
    }
}
