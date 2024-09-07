package pl.supereasy.sectors.core.commands.impl.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.util.ChatUtil;

public class SetSpawnCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public SetSpawnCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        final Player p = (Player) commandSender;
        final Location loc = p.getLocation();
        p.getWorld().setSpawnLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        ChatUtil.sendMessage(p, "&7Ustawiono nowe miejsce respu dla spawna &a" + this.plugin.getSectorManager().getCurrentSector().getSectorName());
        return false;
    }
}
