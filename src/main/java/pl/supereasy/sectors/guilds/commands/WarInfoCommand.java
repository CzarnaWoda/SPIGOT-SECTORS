package pl.supereasy.sectors.guilds.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.inventories.WarInfoInventory;
import pl.supereasy.sectors.guilds.wars.api.War;
import pl.supereasy.sectors.util.ChatUtil;

public class WarInfoCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public WarInfoCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 2) {
            return ChatUtil.sendMessage(commandSender, "Zle uzycie!");
        }
        final Guild g = this.plugin.getGuildManager().getGuild(args[0]);
        if (g == null) {
            String errMessage = GuildConfig.INSTANCE.MESSAGES_GUILDWITHTAGDONTEXIST.replace("{GUILD}", args[0]);
            errMessage = errMessage.replace("{GUILD}", args[0]);
            return ChatUtil.sendMessage(commandSender, errMessage);
        }
        final Guild o = this.plugin.getGuildManager().getGuild(args[1]);
        if (o == null) {
            String errMessage = GuildConfig.INSTANCE.MESSAGES_GUILDWITHTAGDONTEXIST.replace("{GUILD}", args[1]);
            errMessage = errMessage.replace("{GUILD}", args[1]);
            return ChatUtil.sendMessage(commandSender, errMessage);
        }
        final War w = this.plugin.getWarManager().getWar(g, o);
        if (w == null) {
            return ChatUtil.sendMessage(commandSender, " &cTe gildie nie maja ze soba wojny!");
        }
        WarInfoInventory.openMenu((Player) commandSender, w);
        return false;
    }
}
