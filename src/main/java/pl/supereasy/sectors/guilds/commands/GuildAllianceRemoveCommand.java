package pl.supereasy.sectors.guilds.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.guild.GuildAllyRemovePacket;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.util.ChatUtil;

public class GuildAllianceRemoveCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public GuildAllianceRemoveCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 1) {
            return ChatUtil.sendMessage(commandSender, "Zle uzycie");
        }
        final Player player = (Player) commandSender;
        final Guild guild = this.plugin.getGuildManager().getGuild(player.getUniqueId());
        if (guild == null) {
            return ChatUtil.sendMessage(commandSender, GuildConfig.INSTANCE.MESSAGES_YOUDONTHAVEGUILD);
        }
        final Guild otherGuild = this.plugin.getGuildManager().getGuild(args[0]);
        if (otherGuild == null) {
            String errMessage = GuildConfig.INSTANCE.MESSAGES_GUILDWITHTAGDONTEXIST.replace("{GUILD}", args[0]);
            errMessage = errMessage.replace("{GUILD}", args[0]);
            return ChatUtil.sendMessage(commandSender, errMessage);
        }
        if (!this.plugin.getAllianceManager().hasAlliance(guild, otherGuild)) {
            String errMessage = GuildConfig.INSTANCE.MESSAGES_YOUDONTHAVEALLIANCE.replace("{GUILD}", otherGuild.getTag());
            errMessage = errMessage.replace("{GUILD}", otherGuild.getTag());
            return ChatUtil.sendMessage(commandSender, errMessage);
        }
        this.plugin.getAllianceManager().removeAlliance(guild, otherGuild);
        guild.insert(true);
        otherGuild.insert(true);
        final Packet packet = new GuildAllyRemovePacket(guild.getTag(), otherGuild.getTag());
        this.plugin.getSectorClient().sendGlobalPacket(packet);
        return false;
    }
}
