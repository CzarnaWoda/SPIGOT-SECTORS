package pl.supereasy.sectors.core.commands.impl.player;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.util.ChatUtil;

public class FlyCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public FlyCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        final Player p = (Player) commandSender;
        if (args.length == 0) {
            p.setAllowFlight(!p.getAllowFlight());
            return ChatUtil.sendMessage(commandSender, "&7Twoj fly zostal " + (p.getAllowFlight() ? "&awlaczony" : "&cwylaczony"));
        }
        if (args[0] != null) {
            final Player otherPlayer = Bukkit.getPlayer(args[0]);
            if (otherPlayer == null) {
                return ChatUtil.sendMessage(commandSender, "&4Blad: &cGracz jest offline!");
            }
            otherPlayer.setFlying(!otherPlayer.getAllowFlight());
            ChatUtil.sendMessage(commandSender, "&7Fly gracza " + otherPlayer.getDisplayName() + " zostal " + (otherPlayer.getAllowFlight() ? "&awlaczony" : "&cwylaczony"));
            return ChatUtil.sendMessage(otherPlayer, "&7Twoj fly zostal " + (otherPlayer.getAllowFlight() ? "&awlaczony" : "&cwylaczony"));
        }
        return false;
    }
}
