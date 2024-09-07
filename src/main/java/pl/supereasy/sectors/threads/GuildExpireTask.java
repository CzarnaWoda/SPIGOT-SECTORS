package pl.supereasy.sectors.threads;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.guild.GuildRemovePacket;
import pl.supereasy.sectors.api.packets.impl.user.UserStatsUpdatePacket;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.threads.api.Schedulable;
import pl.supereasy.sectors.threads.api.ThreadExecutorAPI;
import pl.supereasy.sectors.util.Time;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class GuildExpireTask extends Schedulable {


    @Override
    public void start() {
        ThreadExecutorAPI.SCHEDULED.scheduleAtFixedRate(() -> {
            for (Object obj : SectorPlugin.getInstance().getGuildManager().getGuilds()) {
                final Guild g = (Guild) obj;
                if (System.currentTimeMillis() >= g.getGuildExpireTime()) {
                    g.delete(true);
                    final GuildRemovePacket guildRemovePacket = new GuildRemovePacket(g.getTag(), "CONSOLE");
                    SectorPlugin.getInstance().getSectorClient().sendGlobalPacket(guildRemovePacket);
                }
            }
        }, 0, 30, TimeUnit.SECONDS);
    }

}
