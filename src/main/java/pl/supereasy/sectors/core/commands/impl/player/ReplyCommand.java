package pl.supereasy.sectors.core.commands.impl.player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.impl.player.PrivateMessagePacket;
import pl.supereasy.sectors.api.redis.channels.RedisChannel;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;

public class ReplyCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public ReplyCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length < 1) {
            return ChatUtil.sendMessage(commandSender, "&8>> &7Poprawne uzycie: &a/r <wiadomosc>");
        }
        final Player p = (Player) commandSender;
        final User user = this.plugin.getUserManager().getUser(p.getUniqueId());
        final User otherUser = user.getChat().getLastConverser();
        if (otherUser == null) {
            return ChatUtil.sendMessage(commandSender, "&8>> &7Nie masz komu odpowiedziec.");
        }
        if (!otherUser.isOnline()) {
            return ChatUtil.sendMessage(commandSender, "&8>> &7Gracz &a" + otherUser.getName() + " &7jest w tej chwili offline.");
        }
        StringBuilder msg = new StringBuilder();
        for (int i = 0; i < args.length; ++i) {
            if (i > 0) {
                msg.append(" ");
            }
            msg.append(args[i]);
        }
        final String message = msg.toString();
        if (otherUser.getSector().getUniqueSectorID() == this.plugin.getSectorManager().getCurrentSector().getUniqueSectorID()) {
            p.sendMessage(ChatUtil.fixColor("&c" + p.getName() + " -> " + otherUser.getName() + " &8>> &c" + message));
            otherUser.asPlayer().sendMessage(ChatUtil.fixColor("&c" + p.getName() + " -> " + otherUser.getName() + " &8>> &c" + message));
            return true;
        }
        final PrivateMessagePacket messagePacket = new PrivateMessagePacket(p.getName(), otherUser.getName(), message);
        this.plugin.getSectorClient().sendGlobalPacket(messagePacket);
        p.sendMessage(ChatUtil.fixColor("&c" + p.getName() + " -> " + otherUser.getName() + " &8>> &c" + message));
        return false;
    }
}
