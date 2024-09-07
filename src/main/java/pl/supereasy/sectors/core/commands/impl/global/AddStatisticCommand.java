package pl.supereasy.sectors.core.commands.impl.global;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;

public class AddStatisticCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public AddStatisticCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup,aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 2) {
            return ChatUtil.sendMessage(commandSender, "Zle uzycie");
        }
        final Player p = (Player) commandSender;
        final User user = this.plugin.getUserManager().getUser(p.getUniqueId());
        final int amount = Integer.parseInt(args[1]);
        switch (args[0]) {
            case "kills":
                user.addKills(amount);
                ChatUtil.sendMessage(commandSender, "Dodano Ci " + amount + " killi!");
                break;
            case "deaths":
                user.addDeaths(amount);
                ChatUtil.sendMessage(commandSender, "Dodano Ci " + amount + " dedow!");
                break;
            case "points":
                user.addPoints(amount);
                ChatUtil.sendMessage(commandSender, "Dodano Ci " + amount + " dedow!");
                break;
            default:
                ChatUtil.sendMessage(commandSender, "Nie ma takiego rodzaju statystyk!");
                break;
        }
        return false;
    }
}
