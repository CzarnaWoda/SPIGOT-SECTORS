package pl.supereasy.sectors.core.commands.impl.global;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.sectors.type.SectorType;
import pl.supereasy.sectors.core.channels.inventory.SectorChannelsInventory;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.util.ChatUtil;

public class ChannelCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public ChannelCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(plugin.getSectorManager().getCurrentSector().getSectorType().equals(SectorType.SPAWN)) {
            SectorChannelsInventory.openInventory((Player) commandSender);
        }else{
            ChatUtil.fixColor("&4Blad: &cNie jestes na sektorze typu SPAWN");
        }
        return false;
    }
}
