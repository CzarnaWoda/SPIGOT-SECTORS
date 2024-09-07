package pl.supereasy.sectors.threads;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.threads.api.Schedulable;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.threads.api.ThreadExecutorAPI;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GuildLiveTask extends Schedulable {


    public void start() {
        ThreadExecutorAPI.SCHEDULED.scheduleAtFixedRate(() -> {
            for (Object currentSectorGuild : SectorPlugin.getInstance().getGuildManager().currentSectorGuilds()) {
                final Guild g = (Guild) currentSectorGuild;
                if (g.getGuildLastBreakHeart() + TimeUnit.MINUTES.toMillis(2) < System.currentTimeMillis()) {
                    g.regenerateHeartPoints(1);
                    g.insert(false);
                }
            }
        }, 0, 15, TimeUnit.SECONDS);
    }

}
