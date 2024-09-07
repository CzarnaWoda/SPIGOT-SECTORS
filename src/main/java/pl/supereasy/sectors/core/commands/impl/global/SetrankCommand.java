package pl.supereasy.sectors.core.commands.impl.global;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.user.UserSetRankPacket;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;

public class SetrankCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public SetrankCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup,aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 2) {
            return ChatUtil.sendMessage(commandSender, " &8» &7Poprawne uzycie: &a/setrank <nick> <ranga>");
        }
        final User user = this.plugin.getUserManager().getUser(args[0]);
        if (user == null) {
            return ChatUtil.sendMessage(commandSender, " &8» &7Gracz o nicku &a" + args[0] + " &7nigdy nie byl na serwerze!");
        }
        UserGroup group;
        try {
            group = UserGroup.valueOf(args[1].toUpperCase());
        } catch (Exception e) {
            return ChatUtil.sendMessage(commandSender, " &8» &7Ranga o nazwie &a" + args[1] + " &7nie istnieje!");
        }
        if (commandSender instanceof Player && (!user.getGroup().hasPermission(group)  || user.getGroup().equals(UserGroup.WLASCICIEL))) {
            return ChatUtil.sendMessage(commandSender, " &8» &7Nie mozesz nadac rangi wyzszej od Twojej!");
        }
        ChatUtil.sendMessage(commandSender, " &8» &7Ustawiono graczowi &a" + user.getName() + " &7range " + group.getGroupPrefix());
        final Packet packet = new UserSetRankPacket(user.getUUID(), commandSender.getName(), group.name());
        this.plugin.getSectorClient().sendGlobalPacket(packet);
        return false;
    }
}
