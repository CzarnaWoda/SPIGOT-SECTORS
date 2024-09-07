package pl.supereasy.sectors.core.commands.impl.user;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;

public class UserSetRankCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public UserSetRankCommand(SectorPlugin plugin, String commandName, UserGroup minGroup) {
        super(plugin, commandName, minGroup);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return false;
    }
}
