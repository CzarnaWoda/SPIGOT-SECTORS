package pl.supereasy.sectors.guilds.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.guild.GuildInvitePacket;
import pl.supereasy.sectors.api.packets.impl.guild.GuildLogPacket;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.guilds.log.enums.GuildLogType;
import pl.supereasy.sectors.util.ChatUtil;

public class GuildInviteCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public GuildInviteCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup,aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 1) {
            return ChatUtil.sendMessage(commandSender, " &8» &7Poprawne uzycie: &e/zapros (nick)");
        }
        final User other = this.plugin.getUserManager().getUser(args[0]);
        if (other == null)
            return ChatUtil.sendMessage(commandSender, " &8» &7Gracz &e" + args[0] + " &7nigdy nie byl na serwerze!");
        final Player p = (Player) commandSender;
        final Guild guild = this.plugin.getGuildManager().getGuild(p.getUniqueId());
        if (guild == null) {
            return ChatUtil.sendMessage(p, GuildConfig.INSTANCE.MESSAGES_YOUDONTHAVEGUILD);
        }
        final Guild guildOther = other.getGuild();
        if (guildOther != null) {
            return ChatUtil.sendMessage(commandSender, " &8» &7Ten gracz posiada juz gildie!");
        }
        if (!guild.getMember(p.getUniqueId()).hasPermission(GuildPermission.MEMBER_INVITE)) {
            return ChatUtil.sendMessage(commandSender, " &8» &cNie posiadasz uprawnien do zapraszania nowych czlonkow do gildii!");
        }
        if (guild.hasInvite(other.getUUID())) {
            return ChatUtil.sendMessage(commandSender, " &8» &7Ten gracz posiada juz aktywne zaproszenie do gildii!");
        }
        ChatUtil.sendMessage(commandSender, " &8» &7Zaprosiles gracza &2" + other.getName() + " &7do gildii! Aby gracz " + other.getName() + " dolaczyl musi wpisac: &e/dolacz TAG GILDII");
        final GuildInvitePacket guildInvitePacket = new GuildInvitePacket(guild.getTag(), other.getUUID(), p.getDisplayName());
        this.plugin.getSectorClient().sendGlobalPacket(guildInvitePacket);
        final User user = this.plugin.getUserManager().getUser(p.getUniqueId());
        this.plugin.getGuildLogger().getLogger(GuildLogType.GUILD_INVITE).update(user, other.getName());
        return true;
    }
}
