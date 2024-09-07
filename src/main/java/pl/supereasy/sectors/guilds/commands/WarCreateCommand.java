package pl.supereasy.sectors.guilds.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.guild.GuildWarStartPacket;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.guilds.wars.api.War;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.DateUtil;

import java.util.concurrent.TimeUnit;

public class WarCreateCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public WarCreateCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
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
            return ChatUtil.sendMessage(commandSender, " &8» &cNie mozesz wypowiedziec wojny swojej gildii!");
        }
        if (otherGuild.getGuildCreateTime() + TimeUnit.HOURS.toMillis(24) > System.currentTimeMillis()) {
            return ChatUtil.sendMessage(player, " &8» &cNie mozesz wypowiedziec wojny nowo zalozonej gildii!");
        }
        if (this.plugin.getWarManager().hasWar(guild, otherGuild)) {
            String errMessage = GuildConfig.INSTANCE.MESSAGES_YOUHAVEWAR;
            errMessage = errMessage.replace("{GUILD}", otherGuild.getTag());
            return ChatUtil.sendMessage(commandSender, errMessage);
        }
        if (!guild.isOwner(player)) {
            return ChatUtil.sendMessage(player, "  &8» &cNie posiadasz uprawnien do tworzenia wojen!");
        }
        final String from = "12:00";
        final String to = "21:00";
        if (!DateUtil.isNowInInterval(from, to)) {
            return ChatUtil.sendMessage(player, "  &8» &cWojne mozna wypowiadac pomiedzy godzina 12:00 a 21:00");
        }
        final War war = this.plugin.getWarManager().createWar(guild, otherGuild);
        war.insert(true);
        final Packet packet = new GuildWarStartPacket(guild.getTag(), guild.getGuildName(), otherGuild.getTag(), otherGuild.getGuildName());
        this.plugin.getSectorClient().sendPacket(packet);
        return true;
    }
}
