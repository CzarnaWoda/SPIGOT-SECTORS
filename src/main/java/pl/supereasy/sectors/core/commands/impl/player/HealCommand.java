package pl.supereasy.sectors.core.commands.impl.player;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.util.ChatUtil;

public class HealCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public HealCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        final Player p = (Player) commandSender;
        if (args.length > 1) {
            return ChatUtil.sendMessage(commandSender, "&8>> &7Poprawne uzycie: &a/heal <nick>");
        }
        if (args.length == 0) {
            p.setHealth(20);
            p.setFoodLevel(20);
            return ChatUtil.sendMessage(commandSender, "&8>> &7Zostales uleczony!");
        }
        final Player otherPlayer = Bukkit.getPlayer(args[0]);
        if (otherPlayer == null) {
            return ChatUtil.sendMessage(commandSender, "&4Blad: &cGracz jest offline!");
        }
        otherPlayer.setHealth(20);
        otherPlayer.setFoodLevel(20);
        ChatUtil.sendMessage(otherPlayer, "&8>> &7Zostales uleczony!");
        ChatUtil.sendMessage(commandSender, "&8>> &7Uleczyles gracza &a" + otherPlayer.getDisplayName());
        return false;
    }
}
