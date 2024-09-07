package pl.supereasy.sectors.core.commands.impl.global;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.adminpanel.inventories.AdminPanelInventory;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;

public class AdminPanelCommand extends CustomCommand {

    public AdminPanelCommand(SectorPlugin plugin, String commandName, UserGroup minGroup) {
        super(plugin, commandName, minGroup);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player){
            final Player player = (Player)sender;
            AdminPanelInventory.openMenu(player);
        }
        return false;
    }
}
