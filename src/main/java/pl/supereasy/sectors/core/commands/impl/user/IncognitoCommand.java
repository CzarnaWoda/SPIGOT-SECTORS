package pl.supereasy.sectors.core.commands.impl.user;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.incognito.inventory.IncognitoInventory;
import pl.supereasy.sectors.core.user.enums.UserGroup;

public class IncognitoCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public IncognitoCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player){
            IncognitoInventory.openMenu((Player) commandSender);
        }
        return false;
    }
}
