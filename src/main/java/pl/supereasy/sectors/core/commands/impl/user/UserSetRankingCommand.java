package pl.supereasy.sectors.core.commands.impl.user;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;

public class UserSetRankingCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public UserSetRankingCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length < 2) {
            return ChatUtil.sendMessage(commandSender, "&7Poprawne uzycie &a/setranking <nick> <punkty>");
        }
        final User user = this.plugin.getUserManager().getUser(args[0]);
        if (user == null) {
            return ChatUtil.sendMessage(commandSender, "&7Uzytkownik &a" + args[0] + " &7nigdy nie byl na serwerze!");
        }
        int setPoints = 0;
        try {
            setPoints = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            return ChatUtil.sendMessage(commandSender, "&4Blad! &cDrugi argument musi byc liczba!");
        }
        //pakiet
        return false;
    }
}
