package pl.supereasy.sectors.guilds.data;

import net.minecraft.server.v1_8_R3.MinecraftServer;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.redis.channels.RedisChannel;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.api.sql.api.Loadable;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.api.GuildMember;
import pl.supereasy.sectors.guilds.builders.GuildBuilder;
import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.guilds.impl.GuildMemberImpl;
import pl.supereasy.sectors.util.GsonUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class GuildMembersData implements Loadable {

    private final SectorPlugin plugin;

    public GuildMembersData(SectorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadData() {
        final AtomicInteger ai = new AtomicInteger(0);
        MinecraftServer.getServer().postToMainThread(() -> {
            RedisChannel.INSTANCE.MEMBERS.forEach(((uuid, s) -> {
                final GuildMemberImpl guildMember = GsonUtil.fromJson(s, GuildMemberImpl.class);
                final Guild g = this.plugin.getGuildManager().getGuild(guildMember.getUUID());
                g.addMember(guildMember.getUUID(), guildMember);
                final User u = this.plugin.getUserManager().getUser(guildMember.getUUID());
                u.setGuild(g);
                ai.incrementAndGet();
            }));
        });
        this.plugin.getLOGGER().log(Level.INFO, "Zaladowano " + ai.get() + " czlonkow gildii!");
    }
}
