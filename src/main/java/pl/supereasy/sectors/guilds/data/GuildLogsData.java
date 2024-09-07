package pl.supereasy.sectors.guilds.data;

import org.bukkit.Bukkit;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.sql.api.Loadable;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.api.GuildMember;
import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.guilds.impl.GuildMemberImpl;
import pl.supereasy.sectors.guilds.log.enums.GuildLogType;
import pl.supereasy.sectors.guilds.log.impl.GuildLogActionImpl;
import pl.supereasy.sectors.util.ChatUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class GuildLogsData implements Loadable {

    private final SectorPlugin plugin;

    public GuildLogsData(SectorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadData() {
        try {
            final AtomicInteger ai = new AtomicInteger(0);

            final ResultSet rs = this.plugin.getMySQL().generateStatement("SELECT * FROM `logs`").executeQuery();
            while (rs.next()) {
                final Guild guild = this.plugin.getGuildManager().getGuild(rs.getString("guildTag"));
                final String memberName = rs.getString("memberName");
                final List<String> logDescription = ChatUtil.fixColor(makeList(rs.getString("logDescription")));
                final Long logTime = rs.getLong("logTime");
                final GuildLogType logType = GuildLogType.valueOf(rs.getString("guildLogType"));
                guild.registerLogAction(new GuildLogActionImpl(guild.getTag(), memberName, logDescription, logTime, logType));
                ai.incrementAndGet();
            }
            this.plugin.getLOGGER().log(Level.INFO, "Zaladowano " + ai.get() + " logow gildii!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private List<String> makeList(String rs) {
        String[] a = rs.split(",");
        return new ArrayList<String>(Arrays.asList(a));
    }
}

