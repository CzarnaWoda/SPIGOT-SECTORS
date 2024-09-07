package pl.supereasy.sectors.core.commands.impl.global;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.global.TpaAcceptAllPacket;
import pl.supereasy.sectors.api.packets.impl.global.TpaAcceptPacket;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TpaAcceptCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public TpaAcceptCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 1) {
            return ChatUtil.sendMessage(commandSender, " &8» &7Poprawne uzycie &a/tpaccept <nick/*>");
            //return ChatUtil.sendMessage(p, " &8» &7Nie masz aktywnych prosb o teleportacje!");
        }
        final Player p = (Player) commandSender;
        final User user = this.plugin.getUserManager().getUser(p.getUniqueId());
        if (user == null) {
            return ChatUtil.sendMessage(p, " &8» &cWystapil blad z profilem relognij!");
        }
        if (args[0].equalsIgnoreCase("*")) {
            if (user.getTpaRequests().size() <= 0) {
                return ChatUtil.sendMessage(p, " &8» &7Nie masz aktywnych prosb o teleportacje!");
            }
            final Set<String> list = new HashSet<>();
            user.getTpaRequests().asMap().forEach((name, time) -> {
                list.add(name);
            });
            final Packet packet = new TpaAcceptAllPacket(user.getUUID(), list);
            this.plugin.getSectorClient().sendGlobalPacket(packet);
            return ChatUtil.sendMessage(commandSender, " &8» &7Zaakceptowales &a" + user.getTpaRequests().size() + " &7prosb o teleportacje.");
        } else {
            if (!user.hasTpaRequestFrom(args[0])) {
                return ChatUtil.sendMessage(p, " &8» &7Nie masz aktywnych prosb o teleportacje od &a" + args[0] + "&7!");
            }
            final Packet packet = new TpaAcceptPacket(user.getUUID(), args[0]);
            this.plugin.getSectorClient().sendGlobalPacket(packet);
            return ChatUtil.sendMessage(commandSender, " &8» &7Zaakceptowales prosbe o teleportacje.");
        }
    }
}
