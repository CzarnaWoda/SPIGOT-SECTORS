package pl.supereasy.sectors.api.teleport.api;

import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.user.impl.User;

public abstract class TeleportRequest {

    private final SectorPlugin plugin;
    private final User teleportUser;
    private final Player teleportPlayer;

    public TeleportRequest(SectorPlugin plugin, User teleportUser) {
        this.plugin = plugin;
        this.teleportUser = teleportUser;
        this.teleportPlayer = this.teleportUser.asPlayer();
    }

    public abstract void reuqestAccepted();

    public SectorPlugin getPlugin() {
        return plugin;
    }

    public User getTeleportUser() {
        return teleportUser;
    }

    public Player getTeleportPlayer() {
        return this.teleportPlayer;
    }
}
