package pl.supereasy.sectors.guilds.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.guild.GuildLeaderChangePacket;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.UUID;

public class GuildLeaderCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public GuildLeaderCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 1) {
            return ChatUtil.sendMessage(commandSender, " &8» &7Poprawne uzycie: &e/lider &8(&enick&8)");
        }
        final Player p = (Player) commandSender;
        final Guild guild = this.plugin.getGuildManager().getGuild(p.getUniqueId());
        if (guild == null) {
            return ChatUtil.sendMessage(commandSender, GuildConfig.INSTANCE.MESSAGES_YOUDONTHAVEGUILD);
        }
        if (!guild.isOwner(p.getUniqueId())) {
            return ChatUtil.sendMessage(commandSender, GuildConfig.INSTANCE.MESSAGES_YOUARENOTGUILDOWNER);
        }
        final User otherUser = this.plugin.getUserManager().getUser(args[0]);
        if (otherUser == null) {
            return ChatUtil.sendMessage(commandSender, " &8» &cTakiego gracza nie bylo nigdy na serwerze!");
        }
        if (guild.isOwner(otherUser.getUUID())) {
            return ChatUtil.sendMessage(commandSender, " &8» &cNie mozesz nadac sobie lidera!");
        }
        if (!guild.isMember(otherUser.getUUID())) {
            return ChatUtil.sendMessage(commandSender, " &8» &cTen gracz nie jest w Twojej gildii!");
        }
        final UUID oldOwnerUUID = guild.getOwnerUUID();
        guild.setOwnerUUID(otherUser.getUUID());
        guild.setOwnerName(otherUser.getName());
        guild.insert(true);
        final Packet packet = new GuildLeaderChangePacket(guild.getTag(), oldOwnerUUID, otherUser.getUUID(), otherUser.getName());
        this.plugin.getSectorClient().sendGlobalPacket(packet);
        return false;
    }
}
