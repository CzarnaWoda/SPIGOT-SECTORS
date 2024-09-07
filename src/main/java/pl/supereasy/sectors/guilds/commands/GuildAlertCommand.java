package pl.supereasy.sectors.guilds.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.redisson.client.RedisClient;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.impl.guild.GuildAlertPacket;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.api.GuildMember;
import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.util.ChatUtil;

public class GuildAlertCommand extends CustomCommand {

    private final SectorPlugin plugin;
    public GuildAlertCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        final Player p = (Player) commandSender;
        final User user = this.plugin.getUserManager().getUser(p.getUniqueId());
        final Guild guild = user.getGuild();
        if (guild == null) {
            return ChatUtil.sendMessage(commandSender, GuildConfig.INSTANCE.MESSAGES_YOUDONTHAVEGUILD);
        }
        final GuildMember member = guild.getMember(user.getUUID());
        if (member == null) {
            return ChatUtil.sendMessage(commandSender, GuildConfig.INSTANCE.MESSAGES_YOUDONTHAVEGUILD);
        }
        if (args.length < 1) {
            return ChatUtil.sendMessage(commandSender, "&4Blad: &cPoprawne uzycie /guildalert <wiadomosc>");
        }
        if (member.hasPermission(GuildPermission.GUILD_ALERT)) {
            String message = StringUtils.join(args, " ");
            final GuildAlertPacket packet = new GuildAlertPacket(message, guild.getTag());
            plugin.getSectorClient().sendGlobalPacket(packet);

        } else {
            ChatUtil.sendMessage(commandSender, "&4Blad: &cNie posiadasz uprawnienia aby to zrobic!");
        }
        return false;
    }
}
