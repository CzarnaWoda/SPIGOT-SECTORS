package pl.supereasy.sectors.core.commands.impl.player;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.impl.player.PlayerToPlayerTeleportPacket;
import pl.supereasy.sectors.api.packets.impl.player.TeleportHerePacket;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;

public class TeleportHereCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public TeleportHereCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            //stp nick
            if (args.length < 1) {
                return ChatUtil.sendMessage(commandSender, "&4Blad: &cPoprawne uzycie komendy: /tphere <player>");
            }
            final Player player = (Player) commandSender;
            final User user = plugin.getUserManager().getUser(args[0]);
            if (user == null) {
                return ChatUtil.sendMessage(player, "&4Blad: &cNie znaleziono takiego uzytkownika na serwerze");
            }
            if (!user.isOnline()) {
                return ChatUtil.sendMessage(player, "&4Blad: &cTen uzytkownik nie jest online na serwerze");
            }

            final TeleportHerePacket packet = new TeleportHerePacket(player.getUniqueId(),user.getUUID());
            plugin.getSectorClient().sendGlobalPacket(packet);

            return ChatUtil.sendMessage(player,"&7Przeteleportowales gracza &a" + user.getName() + " &7do siebie");
        }
        return false;
    }
}
