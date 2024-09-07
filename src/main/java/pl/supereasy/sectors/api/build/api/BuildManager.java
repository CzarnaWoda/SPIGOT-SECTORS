package pl.supereasy.sectors.api.build.api;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.api.build.enums.CanBuild;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;

public interface BuildManager {

    CanBuild isAllowBuild(final Player player, final User user, final Block block, final Guild guild);

}
