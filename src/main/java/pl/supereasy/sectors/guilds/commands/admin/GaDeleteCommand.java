package pl.supereasy.sectors.guilds.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.blazingpack.cuboids.BlazingCuboidAPI;
import pl.supereasy.sectors.api.packets.impl.guild.GuildRemovePacket;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.util.ChatUtil;

public class GaDeleteCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public GaDeleteCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 1) {
            return ChatUtil.sendMessage(commandSender, " &8» &7Poprawne uzycie: &e/galeader (tag)");
        }
        final Guild guild = this.plugin.getGuildManager().getGuild(args[0]);
        if (guild == null) {
            return ChatUtil.sendMessage(commandSender, GuildConfig.INSTANCE.MESSAGES_GUILDWITHTAGDONTEXIST.replace("{GUILD}", args[0]));
        }
        final GuildRemovePacket guildRemovePacket = new GuildRemovePacket(guild.getTag(), commandSender.getName());
        this.plugin.getSectorClient().sendGlobalPacket(guildRemovePacket);
        BlazingCuboidAPI.reloadCuboidsForAllPlayers();
        return true;
    }
}
