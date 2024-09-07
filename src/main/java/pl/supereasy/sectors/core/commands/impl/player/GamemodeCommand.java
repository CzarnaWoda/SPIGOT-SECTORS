package pl.supereasy.sectors.core.commands.impl.player;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;

public class GamemodeCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public GamemodeCommand(SectorPlugin plugin, String commandName, UserGroup minGroup) {
        super(plugin, commandName, minGroup, "gm");
        this.plugin = plugin;
    }


    public GamemodeCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        final Player p = (Player) commandSender;
        if (args.length < 1 || args.length > 2) {
            return ChatUtil.sendMessage(commandSender, "&4Blad: &cPoprawne uzycie komendy: /gamemode <tryb> <nick>");
        }
        final GameMode gameMode = GameMode.getByValue(Integer.parseInt(args[0]));
        if (gameMode == null) {
            return ChatUtil.sendMessage(commandSender, "&4Blad: &cNie ma takiego trybu gamemode!");
        }
        if (args.length == 1) {
            p.setGameMode(gameMode);
            return ChatUtil.sendMessage(commandSender, "&8>> &7Twoj tryb gamemode zostal zmieniony na &a" + gameMode.name());
        }
        final User user = this.plugin.getUserManager().getUser(args[1]);
        if (user == null) {
            return ChatUtil.sendMessage(commandSender, "&8>> &7Gracz &a" + args[1] + " &7nigdy nie byl na serwerze!");
        }
        final Player otherPlayer = user.asPlayer();
        if (otherPlayer == null || user.getSector().getUniqueSectorID() != this.plugin.getSectorManager().getCurrentSector().getUniqueSectorID()) {
            return ChatUtil.sendMessage(commandSender, "&8>> &7Gracz &a" + args[1] + " &7jest na innym sektorze(&a#" + user.getSector().getUniqueSectorID() + " " + user.getSector().getSectorName() + ")");
        }
        otherPlayer.setGameMode(gameMode);
        ChatUtil.sendMessage(commandSender, "&8>> &7Zmieniles tryb gry graczowi &a" + otherPlayer.getDisplayName() + " &7na &a" + gameMode.name());
        ChatUtil.sendMessage(otherPlayer, "&8>> &7Twoj tryb gamemode zostal zmieniony na &a" + gameMode.name());
        //TODO packet playerNBTUpdate -> NBT String, T value
        return false;
    }
}
