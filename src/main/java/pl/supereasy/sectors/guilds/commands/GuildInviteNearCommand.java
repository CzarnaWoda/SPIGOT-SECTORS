package pl.supereasy.sectors.guilds.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.impl.guild.GuildInvitePacket;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.guilds.log.enums.GuildLogType;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.LocationUtil;
import pl.supereasy.sectors.util.Util;

import java.util.List;

public class GuildInviteNearCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public GuildInviteNearCommand(SectorPlugin plugin, String commandName, UserGroup minGroup) {
        super(plugin, commandName, minGroup);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 1) {
            return ChatUtil.sendMessage(commandSender, " &8» &7Poprawne uzycie: &e/zaprosall <zasieg (max 10)>");
        }
        if (!Util.isInteger(args[0])) {
            return ChatUtil.sendMessage(commandSender, " &8» &7Poprawne uzycie: &e/zaprosall <zasieg (max 10)>");
        }
        final Player p = (Player) commandSender;
        final User user = this.plugin.getUserManager().getUser(p.getUniqueId());
        final Guild guild = user.getGuild();
        if (guild == null) {
            return ChatUtil.sendMessage(p, GuildConfig.INSTANCE.MESSAGES_YOUDONTHAVEGUILD);
        }
        final int amount = Integer.parseInt(args[0]);
        if (amount > 10) {
            return ChatUtil.sendMessage(commandSender, " &8» &7Poprawne uzycie: &e/zaprosall <zasieg (max 10)>");
        }
        if (!guild.getMember(p.getUniqueId()).hasPermission(GuildPermission.MEMBER_INVITE)) {
            return ChatUtil.sendMessage(commandSender, " &8» &cNie posiadasz uprawnien do zapraszania nowych czlonkow do gildii!");
        }
        final List<Player> players = LocationUtil.getPlayersInRadiusNoStonePlate(p.getLocation(),amount);
        int index = 0;
        for(Player player : players){
            final User other = plugin.getUserManager().getUser(player.getUniqueId());
            if (other != null && !other.getUUID().equals(p.getUniqueId())) {
                final Guild guildOther = other.getGuild();
                if (guildOther == null && !guild.hasInvite(other.getUUID())) {
                    ChatUtil.sendMessage(commandSender, " &8» &7Zaprosiles gracza &2" + other.getName() + " &7do gildii! Aby gracz " + other.getName() + " mogl dolaczyc musi wpisac: &e/dolacz TAG GILDII");
                    //final GuildInvitePacket guildInvitePacket = new GuildInvitePacket(guild.getTag(), other.getUUID(), player.getDisplayName());
                    //this.plugin.getSectorClient().sendGlobalPacket(guildInvitePacket);
                    guild.getInvites().put(other.getUUID(), p.getDisplayName());
                    this.plugin.getGuildLogger().getLogger(GuildLogType.GUILD_INVITE).update(user, user.getName());
                    ChatUtil.sendMessage(player, " &2&lGILDIE &8->> &7Zostałeś zaproszony do gildii &8[&a" + guild.getTag() + "&8] &7wpisz: &a/dolacz " + guild.getTag() + "&7 aby dolaczyc.");
                    index++;
                }
            }
        }
        ChatUtil.sendMessage(commandSender, " &8» &7Zaprosiles &8(&c" + index + "&8) &7graczy do swojej gildii!");
        return true;
    }
}
