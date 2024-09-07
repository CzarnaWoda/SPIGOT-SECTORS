package pl.supereasy.sectors.core.commands.impl.global;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.tnt.TntInventory;
import pl.supereasy.sectors.core.user.enums.UserGroup;

public class TntCommand extends CustomCommand {

    private final SectorPlugin plugin;
    public TntCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);

        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player){
            TntInventory.openInventory((Player) sender);
        }
        return false;
    }
}
