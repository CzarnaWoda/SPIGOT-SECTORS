package pl.supereasy.sectors.core.commands.impl.player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.shops.inventories.ShopInventory;
import pl.supereasy.sectors.core.user.enums.UserGroup;

public class ShopCommand extends CustomCommand {
    public ShopCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup,aliases);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player){
            if(SectorPlugin.getInstance().getCoreConfig().isDiamondItems()) {

                ShopInventory.openMenu((Player) commandSender);
            }
        }
        return false;
    }
}
