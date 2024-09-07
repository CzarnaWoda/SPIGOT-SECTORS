package pl.supereasy.sectors.core.commands.impl.global;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.global.TpaRequestPacket;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;

public class TpaCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public TpaCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 1) {
            return ChatUtil.sendMessage(commandSender, " &8» &7Poprawne uzycie: &a/tpa <nick>");
        }
        final Player player = (Player) commandSender;
        final User userRequested = this.plugin.getUserManager().getUser(args[0]);
        if (player.getDisplayName().equalsIgnoreCase(args[0])) {
            return ChatUtil.sendMessage(commandSender, " &8» &7Nie mozesz wyslac prosby o teleportacje do samego siebie!");
        }
        if (userRequested == null) {
            return ChatUtil.sendMessage(commandSender, " &8» &7Gracz o nicku &e" + args[0] + " &7nigdy nie byl na serwerze!");
        }
        if (!userRequested.getSector().isOnline(userRequested.getName())) {
            return ChatUtil.sendMessage(commandSender, " &8» &7Gracz jest offline!");
        }
        if (userRequested.hasTpaRequestFrom(player.getName())) {
            return ChatUtil.sendMessage(commandSender, " &8» &7Ten gracz posiada juz prosble o teleportacje od Ciebie!");
        }
        if (!userRequested.getSector().isSectorOnline()) {
            return ChatUtil.sendMessage(commandSender, "&4Blad: &7Sektor &8(&a" + userRequested.getSector().getSectorName() + "&8) &7jest obecnie wylaczony!");
        }
        ChatUtil.sendMessage(commandSender, " &8» &7Wyslales prosbe o teleportacje do gracza &a" + userRequested.getName());
        final Packet packet = new TpaRequestPacket(userRequested.getUUID(), player.getUniqueId());
        this.plugin.getSectorClient().sendGlobalPacket(packet);
        return false;
    }
}
