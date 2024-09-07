package pl.supereasy.sectors.core.commands.impl.player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.effects.EffectInventory;
import pl.supereasy.sectors.core.user.enums.UserGroup;

public class EffectsCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public EffectsCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        //EffectInventory.getEffectsMainMenu().openInventory((Player) commandSender);
        if(commandSender instanceof Player) {
            EffectInventory.openMenu((Player) commandSender);
        }
        return false;
    }
}
