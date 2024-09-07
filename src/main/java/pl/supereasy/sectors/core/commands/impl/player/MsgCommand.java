package pl.supereasy.sectors.core.commands.impl.player;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.impl.player.PrivateMessagePacket;
import pl.supereasy.sectors.api.redis.channels.RedisChannel;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.MessageType;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.ArrayList;
import java.util.List;

public class MsgCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public MsgCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length < 2) {
            return ChatUtil.sendMessage(commandSender, "&8>> &7Poprawne uzycie: &a/msg <gracz> <wiadomosc>");
        }
        final Player p = (Player) commandSender;
        final User otherUser = this.plugin.getUserManager().getUser(args[0]);
        if (otherUser == null) {
            return ChatUtil.sendMessage(commandSender, "&8>> &7Gracz o nicku &a" + args[0] + " &7nie byl na serwerze!");
        }
        if (!otherUser.isOnline()) {
            return ChatUtil.sendMessage(commandSender, "&8>> &7Gracz jest offline.");
        }
        if (otherUser.isIgnoring(p.getUniqueId())) {
            return ChatUtil.sendMessage(commandSender, "&4Blad! &cTen gracz ignoruje Ciebie.");
        }
        if (otherUser.getChat().hasDisabled(MessageType.MSG)) {
            return ChatUtil.sendMessage(commandSender, "&4Blad! &cTen gracz ignoruje wiadomości prywatne.");
        }
        StringBuilder msg = new StringBuilder();
        for (int i = 1; i < args.length; ++i) {
            if (i > 1) {
                msg.append(" ");
            }
            msg.append(args[i]);
        }
        final String message = msg.toString();
        final User user = this.plugin.getUserManager().getUser(p.getUniqueId());
        if (otherUser.getSector().getUniqueSectorID() == this.plugin.getSectorManager().getCurrentSector().getUniqueSectorID()) {
            final Player otherPlayer = Bukkit.getPlayer(args[0]);
            if (otherPlayer == null) {
                return ChatUtil.sendMessage(commandSender, "&8>> &7Gracz nie jest online na sektorze &8(&a" + this.plugin.getSectorManager().getCurrentSector().getSectorName() + "&8)");
            }
            p.sendMessage(ChatUtil.fixColor("&c" + p.getName() + " -> " + otherUser.getName() + " &8>> &c" + message));
            otherPlayer.sendMessage(ChatUtil.fixColor("&c" + p.getName() + " -> " + otherUser.getName() + " &8>> &c" + message));
            user.getChat().setLastConverser(otherUser);
            otherUser.getChat().setLastConverser(user);
            return true;
        }
        final PrivateMessagePacket messagePacket = new PrivateMessagePacket(p.getName(), otherUser.getName(), message);
        this.plugin.getSectorClient().sendGlobalPacket(messagePacket);
        p.sendMessage(ChatUtil.fixColor("&c" + p.getName() + " -> " + otherUser.getName() + " &8>> &c" + message));
        user.getChat().setLastConverser(otherUser);
        otherUser.getChat().setLastConverser(user);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        final List<String> matches = new ArrayList<>();
        if (args.length == 1) {
            final String search = args[0].toLowerCase();
            for (String online : RedisChannel.INSTANCE.onlineGlobalPlayers) {
                if (online.startsWith(search)) {
                    matches.add(online);
                }
            }
        }
        return matches;
    }

}
