package pl.supereasy.sectors.core.commands.impl.player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.core.warp.api.Warp;
import pl.supereasy.sectors.util.ChatUtil;

public class WarpCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public WarpCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 1) {
            return ChatUtil.sendMessage(commandSender, "&4Blad! &cPoprawne uzycie /warp <nazwa>");
        }
        Warp warp = this.plugin.getWarpManager().getWarp(args[0]);
        if (warp == null) {
            return ChatUtil.sendMessage(commandSender, "&4Blad! &cWarp o nazwie " + args[0] + " nie istnieje!");
        }
        final Player player = (Player) commandSender;
        final User teleportUser = this.plugin.getUserManager().getUser(player.getUniqueId());
        if (teleportUser == null) {
            player.kickPlayer("Wystapil blad z Twoim profilem. Jezeli blad nie ustanie zglos sie do administracji!");
            return ChatUtil.sendMessage(commandSender, "&cJakim cudem Ty grasz? XD");
        }
        this.plugin.getTeleportManager().teleportPlayer(teleportUser, warp.getLocation(), warp.getSector());
        return false;
    }
}
