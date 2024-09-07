package pl.supereasy.sectors.guilds.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.guild.GuildAllyCreatePacket;
import pl.supereasy.sectors.api.packets.impl.guild.GuildAllyRequestPacket;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.alliances.impl.AllianceImpl;
import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.UUID;

public class GuildAllianceCommand extends CustomCommand {

    private final SectorPlugin plugin;


    public GuildAllianceCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 1) {
            return ChatUtil.sendMessage(commandSender, "Zle uzycie!");
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
        if (guild.equals(otherGuild)) {
            return ChatUtil.sendMessage(commandSender, " &8» &cNie mozesz wyslac prosby sojuszu do swojej gildii!");
        }
        if (this.plugin.getAllianceManager().hasAlliance(otherGuild, guild)) {
            String errMessage = GuildConfig.INSTANCE.MESSAGES_YOUHAVEALLIANCE;
            errMessage = errMessage.replace("{GUILD}", otherGuild.getTag());
            return ChatUtil.sendMessage(commandSender, errMessage);
        }
        if (!guild.getMember(player.getUniqueId()).hasPermission(GuildPermission.ALLIANCE_INVITE) || !guild.isOwner(player)) {
            return ChatUtil.sendMessage(player, "  &8» &cNie posiadasz uprawnien do tworzenia sojuszy!");
        }
        if (otherGuild.hasAllianceInvite(guild.getTag())) {
            return ChatUtil.sendMessage(player, " &8» &7Gildia &e" + otherGuild.getTag() + " &7posiada juz aktywna prosbe o sojusz!");
        }
        if (guild.hasAllianceInvite(otherGuild.getTag())) {
            final UUID uuid = UUID.randomUUID();
            final AllianceImpl alliance = new AllianceImpl(uuid, guild, otherGuild);
            alliance.insert(true);
            final Packet packet = new GuildAllyCreatePacket(uuid, guild.getTag(), otherGuild.getTag());
            this.plugin.getSectorClient().sendGlobalPacket(packet);
            return true;
        }
        final Packet packet = new GuildAllyRequestPacket(player.getUniqueId(), guild.getTag(), otherGuild.getTag());
        this.plugin.getSectorClient().sendGlobalPacket(packet);
        return true;
    }

}
