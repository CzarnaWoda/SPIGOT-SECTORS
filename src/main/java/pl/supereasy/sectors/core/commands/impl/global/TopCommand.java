package pl.supereasy.sectors.core.commands.impl.global;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.tops.inventory.TopInventory;
import pl.supereasy.sectors.core.user.enums.UserGroup;

public class TopCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public TopCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        TopInventory.openTopInventory((Player) commandSender);
        return false;
    }
}
