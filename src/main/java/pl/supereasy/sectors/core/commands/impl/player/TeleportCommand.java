package pl.supereasy.sectors.core.commands.impl.player;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.redis.channels.RedisChannel;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.ArrayList;
import java.util.List;

public class TeleportCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public TeleportCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length < 1) {
            return ChatUtil.sendMessage(commandSender, "&4Blad! &cPoprawne uzycie /tp <nick>");
        }
        if (args.length == 1) {
            final User targetUser = this.plugin.getUserManager().getUser(args[0]);
            if (targetUser == null) {
                return ChatUtil.sendMessage(commandSender, "&4Blad! &cGracza nigdy nie bylo na serwerze.");
            }
            if (!targetUser.getSector().getOnlinePlayers().contains(targetUser.getName())) {
                return ChatUtil.sendMessage(commandSender, "&4Blad! &cGracz jest offline.");
            }
            final Player p = (Player) commandSender;
            final User user = this.plugin.getUserManager().getUser(p.getUniqueId());
            this.plugin.getTeleportManager().teleportPlayer(user, targetUser, false);
        }
        if (args.length == 2) {
            //TODO
        }
        return false;
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
