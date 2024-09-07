package pl.supereasy.sectors.core.commands.impl.user;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.deposit.inventory.DepositInventory;
import pl.supereasy.sectors.core.user.enums.UserGroup;

public class DepositCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public DepositCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup,aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player){
            final Player player = (Player)sender;

            DepositInventory.openMenu(player);
        }
        return false;
    }
}
