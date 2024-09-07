package pl.supereasy.sectors.guilds.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.guilds.inventories.GuildFreeAreaInventory;

public class GuildFreeAreaCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public GuildFreeAreaCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        GuildFreeAreaInventory.getFreeAreaGUI().openInventory((Player) commandSender);
        return false;
    }
}
