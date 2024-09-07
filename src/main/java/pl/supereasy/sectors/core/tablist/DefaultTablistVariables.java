package pl.supereasy.sectors.core.tablist;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.redis.channels.RedisChannel;
import pl.supereasy.sectors.core.events.enums.EventType;
import pl.supereasy.sectors.core.events.managers.EventManager;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.function.Consumer;

public final class DefaultTablistVariables
{
    private static final Collection<Consumer<TablistVariablesParser>> installers;

    public static void install(final TablistVariablesParser parser) {
        parser.add(new TimeFormattedVariable("HOUR", user -> Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));
        parser.add(new TimeFormattedVariable("MINUTE", user -> Calendar.getInstance().get(Calendar.MINUTE)));
        parser.add(new TimeFormattedVariable("SECOND", user -> Calendar.getInstance().get(Calendar.SECOND)));
        parser.add(new SimpleTablistVariable("PLAYER", User::getName));
        parser.add(new SimpleTablistVariable("KILLS", user -> String.valueOf(user.getKills())));
        parser.add(new SimpleTablistVariable("DEATHS", user -> String.valueOf(user.getDeaths())));
        parser.add(new SimpleTablistVariable("LOGOUTS", user -> String.valueOf(user.getLogouts())));
        parser.add(new SimpleTablistVariable("KDR", user -> String.valueOf(user.getKd())));
        parser.add(new SimpleTablistVariable("LEVEL", user -> String.valueOf(user.getLevel())));
        parser.add(new SimpleTablistVariable("MONEY", user -> String.valueOf(user.getCoins())));
        parser.add(new SimpleTablistVariable("PLAYS", user -> String.valueOf(user.getPoints())));
        parser.add(new SimpleTablistVariable("POINTS", user -> String.valueOf(user.getPoints())));
        parser.add(new SimpleTablistVariable("SECTOR", user -> SectorPlugin.getInstance().getSectorManager().getCurrentSector().getSectorName()));
        parser.add(new SimpleTablistVariable("PING", user -> String.valueOf(((CraftPlayer) Bukkit.getPlayer(user.getName())).getHandle().ping)));
        final EventManager eventManager = SectorPlugin.getInstance().getEventManager();
        parser.add(new SimpleTablistVariable("ISEVENT", user -> ChatUtil.fixColor(eventManager.isOnEvent(EventType.TURBODROP) || eventManager.isOnEvent(EventType.TURBOEXP) ? ChatColor.GREEN + "%V%" : ChatColor.RED + "%X%")));
        parser.add(new GuildDependentTablistVariable("G-TAG", user -> SectorPlugin.getInstance().getGuildManager().getGuild(user.getUUID()).getTag(), user -> "BRAK"));
        parser.add(new GuildDependentTablistVariable("G-POINTS", user -> String.valueOf(SectorPlugin.getInstance().getGuildManager().getGuild(user.getUUID()).getPoints()), user -> "BRAK"));
        parser.add(new GuildDependentTablistVariable("G-KILLS", user -> String.valueOf(SectorPlugin.getInstance().getGuildManager().getGuild(user.getUUID()).getKills()), user -> "BRAK"));
        parser.add(new GuildDependentTablistVariable("G-DEATHS", user -> String.valueOf(SectorPlugin.getInstance().getGuildManager().getGuild(user.getUUID()).getDeaths()), user -> "BRAK"));
        parser.add(new GuildDependentTablistVariable("G-KDR", user -> String.valueOf(SectorPlugin.getInstance().getGuildManager().getGuild(user.getUUID()).getKDr()), user -> "BRAK"));
        parser.add(new GuildDependentTablistVariable("G-ONLINE", user -> String.valueOf(SectorPlugin.getInstance().getGuildManager().getGuild(user.getUUID()).getOnlineMembers().size()), user -> "BRAK"));
        parser.add(new GuildDependentTablistVariable("G-MEMBERS", user -> String.valueOf(SectorPlugin.getInstance().getGuildManager().getGuild(user.getUUID()).getMembers().size()), user -> "BRAK"));
        parser.add(new SimpleTablistVariable("TABLISTTYPE", user -> TabUpdateTask.type.equals(TabListType.KILLS) ? "ZABOJSTWA" : TabUpdateTask.type.equals(TabListType.EATKOX) ? "ZJEDZONE KOXY" : TabUpdateTask.type.equals(TabListType.POINTS) ? "RANKING PVP" : "WYKOPANY KAMIEN"));
        parser.add(new SimpleTablistVariable("RANK", user -> user.getGroup().name().toUpperCase()));
        parser.add(new SimpleTablistVariable("ONLINE", user -> String.valueOf(RedisChannel.INSTANCE.onlineGlobalPlayers.size())));
        parser.add(new SimpleTablistVariable("TPS", user -> ChatUtil.fixColor(ChatColor.GREEN + "%V%")));
        parser.add(new SimpleTablistVariable("USERS", user -> String.valueOf(SectorPlugin.getInstance().getUserManager().getUsers().size())));

        for (final Consumer<TablistVariablesParser> installer : DefaultTablistVariables.installers) {
            installer.accept(parser);
        }
    }

    public static void registerInstaller(final Consumer<TablistVariablesParser> parser) {
        DefaultTablistVariables.installers.add(parser);
    }

    static {
        installers = new ArrayList<>();
    }
}
