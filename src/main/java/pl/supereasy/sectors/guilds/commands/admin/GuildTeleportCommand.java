package pl.supereasy.sectors.guilds.commands.admin;

import com.google.common.cache.CacheBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.concurrent.TimeUnit;

public class GuildTeleportCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public GuildTeleportCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 1) {
            return ChatUtil.sendMessage(commandSender, " &8» &7Poprawne uzycie: &e/gatp (TAG)");
        }
        final Guild guild = this.plugin.getGuildManager().getGuild(args[0]);
        if (guild == null) {
            return ChatUtil.sendMessage(commandSender, GuildConfig.INSTANCE.MESSAGES_GUILDWITHTAGDONTEXIST.replace("{GUILD}", args[0]));
        }
        final Player player = (Player) commandSender;
        final User user = this.plugin.getUserManager().getUser(player.getUniqueId());
        if (guild.getHomeLocation() == null) {
            this.plugin.getTeleportManager().teleportPlayer(user, guild.getGuildRegion().getCenter(), guild.getGuildSector());
            return ChatUtil.sendMessage(commandSender, " &8» &7Przeteleportowano do centrum gildii &8[&e" + guild.getTag() + "&8]");
        }
        this.plugin.getTeleportManager().teleportPlayer(user, guild.getHomeLocation(), guild.getGuildSector());
        ChatUtil.sendMessage(commandSender, " &8» &7Przeteleportowano do domu gildii &8[&e" + guild.getTag() + "&8]");
        return false;
    }
}
