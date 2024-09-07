package pl.supereasy.sectors.guilds.commands;

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
import pl.supereasy.sectors.util.TimeUtil;

import java.util.UUID;

public class GuildInfoCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public GuildInfoCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 1) {
            return ChatUtil.sendMessage(commandSender, " &8» &7Poprawne uzycie: &e/info (TAG)");
        }
        final Guild guild = this.plugin.getGuildManager().getGuild(args[0]);
        if (guild == null) {
            return ChatUtil.sendMessage(commandSender, GuildConfig.INSTANCE.MESSAGES_GUILDWITHTAGDONTEXIST.replace("{GUILD}", args[0]));
        }
        final Player p = (Player) commandSender;
        guild.sendInfoMessage(p);
        return false;
    }
}
