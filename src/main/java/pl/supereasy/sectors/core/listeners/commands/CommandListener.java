package pl.supereasy.sectors.core.listeners.commands;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.util.ChatUtil;

public class CommandListener implements Listener {

    private final SectorPlugin plugin;

    public CommandListener(SectorPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCommand(final PlayerCommandPreprocessEvent e) {
        final Player p = e.getPlayer();
        final Guild guild = this.plugin.getGuildManager().getGuild(p.getLocation());
        if (guild == null) return;
        final User u = this.plugin.getUserManager().getUser(p.getUniqueId());
        final String command = e.getMessage().split(" ")[0];
        if ((u.getGuild() == null || (u.getGuild() != null && !u.getGuild().equals(guild))) && GuildConfig.INSTANCE.COMMANDS_GUILDAREA_BLOCKEDCMDS.contains(command)) {
            e.setCancelled(true);
            ChatUtil.sendMessage(p, GuildConfig.INSTANCE.COMMANDS_AREA_BLOCKMESSAGE);
        }
    }

}
