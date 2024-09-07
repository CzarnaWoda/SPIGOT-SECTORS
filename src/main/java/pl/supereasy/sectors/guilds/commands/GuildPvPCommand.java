package pl.supereasy.sectors.guilds.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.guild.GuildAllySetPvPPacket;
import pl.supereasy.sectors.api.packets.impl.guild.GuildSetPvPPacket;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.util.ChatUtil;

public class GuildPvPCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public GuildPvPCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        final Player p = (Player) commandSender;
        final Guild guild = this.plugin.getGuildManager().getGuild(p.getUniqueId());
        if (guild == null) {
            return ChatUtil.sendMessage(commandSender, GuildConfig.INSTANCE.MESSAGES_YOUDONTHAVEGUILD);
        }
        if (!guild.getMember(p.getUniqueId()).hasPermission(GuildPermission.PVP)) {
            return ChatUtil.sendMessage(commandSender, " &8» &cNie posiadasz uprawnien do zmieniania statusu PVP!");
        }
        if (args.length == 0) {
            final GuildSetPvPPacket packet = new GuildSetPvPPacket(guild.getTag(), p.getDisplayName(), !guild.isFriendlyFire());
            this.plugin.getSectorClient().sendGlobalPacket(packet);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("sojusz")) {
            final Packet packet = new GuildAllySetPvPPacket(guild.getTag(), p.getName(), !guild.isAllianceFriendlyFire());
            this.plugin.getSectorClient().sendGlobalPacket(packet);
        } else {
            return ChatUtil.sendMessage(p, " &8» &7Poprawne uzycie: &a/pvp <sojusz>");
        }
        return false;
    }
}
