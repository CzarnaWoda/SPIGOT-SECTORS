package pl.supereasy.sectors.core.commands.impl.player;

import io.vavr.collection.Set;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.api.sectors.type.SectorType;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.Comparator;
import java.util.Optional;

public class SpawnCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public SpawnCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        final Player p = (Player) commandSender;
        final User user = SectorPlugin.getInstance().getUserManager().getUser(p.getUniqueId());
        if (user.getSector().getSectorType() == SectorType.SPAWN) {
            this.plugin.getTeleportManager().teleportPlayer(user, p.getWorld().getSpawnLocation(), user.getSector());
            return true;
        }
        this.plugin.getTeleportManager().teleportToSpawn(user, p, false);
        return false;
    }
}
