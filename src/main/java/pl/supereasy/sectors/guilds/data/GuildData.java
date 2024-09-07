package pl.supereasy.sectors.guilds.data;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.redis.channels.RedisChannel;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.api.sql.api.Loadable;
import pl.supereasy.sectors.api.sql.api.Saveable;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.builders.GuildBuilder;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.GsonUtil;
import pl.supereasy.sectors.util.ItemStackUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class GuildData implements Loadable, Saveable {

    private final SectorPlugin plugin;

    public GuildData(SectorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadData() {
        final AtomicInteger ai = new AtomicInteger(0);
        RedisChannel.INSTANCE.GUILDS.forEach((tag, g) -> {
            final Guild guild = GsonUtil.fromJson(g, Guild.class);
            guild.getGuildTreasury().setup();
            this.plugin.getGuildManager().registerGuild(guild);
            guild.getMembers().forEach(((uuid, guildMember) -> {
                final User user = this.plugin.getUserManager().getUser(guildMember.getUUID());
                user.setGuild(guild);
            }));
            ai.getAndIncrement();
        });
        this.plugin.getLogger().log(Level.INFO, "Zaladowano " + ai.get() + " gildii!");
    }

    @Override
    public void saveData() {
        final AtomicInteger ai = new AtomicInteger(0);
        for (Object guildObj : this.plugin.getGuildManager().currentSectorGuilds()) {
            final Guild guild = (Guild) guildObj;
            guild.insert(false);
            ai.getAndIncrement();
        }
        this.plugin.getLogger().log(Level.INFO, "Zapisano " + ai.get() + " gildii!");
    }
}
