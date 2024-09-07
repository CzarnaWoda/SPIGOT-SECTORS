package pl.supereasy.sectors.core.commands.impl.player;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.util.ChatUtil;

public class UnstuckCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public UnstuckCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        final Player p = (Player) commandSender;
        if (this.plugin.getCombatManager().getCombat(p).hasFight()) {
            return ChatUtil.sendMessage(p, "&8» &cNie mozesz tego zrobic podczas walki!");
        }
        final Location location = p.getLocation();
        final Sector sector = this.plugin.getSectorManager().getCurrentSector();
        if (!sector.isNearBorder(location, 15)) {
            return ChatUtil.sendMessage(p, "&8» &cTa komende mozesz tylko uzyc na granicy sektora!");
        }
        return true;
    }
}
