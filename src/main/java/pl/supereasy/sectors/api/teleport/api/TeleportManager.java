package pl.supereasy.sectors.api.teleport.api;

import com.google.common.cache.Cache;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.core.user.impl.User;

import java.util.List;
import java.util.UUID;

public interface TeleportManager {

    void teleportPlayer(final User teleportUser, final User targetUser, final boolean instant);

    void teleportPlayer(final User teleportUser, final Location location, final Sector sector);

    void deleteRequestIfExists(final UUID uuid);

    default void deleteRequestIfExists(final Player player) {
        deleteRequestIfExists(player.getUniqueId());
    }


    boolean hasTeleportRequest(final UUID uuid);

    void registerRequest(final UUID requestedPlayer, final Teleportable teleportable);

    void teleportToSpawn(final User user, final Player player, final boolean instant);

    void teleportToRandomSector(final User user, final Player player);

    void teleportPlayersToRandomSector(final List<User> users);

    default void teleportToSpawn(final Player player, final boolean instant) {
        teleportToSpawn(SectorPlugin.getInstance().getUserManager().getUser(player.getUniqueId()), player, instant);
    }

    Teleportable getRequest(final UUID uuid);
}
