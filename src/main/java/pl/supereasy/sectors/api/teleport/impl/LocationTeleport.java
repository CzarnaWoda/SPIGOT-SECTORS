package pl.supereasy.sectors.api.teleport.impl;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.impl.player.PlayerTeleportPacket;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.api.teleport.api.TeleportRequest;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.TimeUtil;

public class LocationTeleport extends TeleportRequest {

    private final Sector sector;
    private final Location teleportLocation;

    public LocationTeleport(SectorPlugin plugin, User teleportUser, Sector sector, Location teleportLocation) {
        super(plugin, teleportUser);
        this.sector = sector;
        this.teleportLocation = teleportLocation;
    }

    @Override
    public void reuqestAccepted() {
        if (this.sector.getUniqueSectorID() == this.getPlugin().getCurrentSectorConfig().getSectorUniqueID()) {
            getTeleportUser().setProtection(TimeUtil.parseDateDiff("5s",true));
            getTeleportUser().setUserLocation(this.teleportLocation);

            this.getTeleportPlayer().teleport(this.teleportLocation);
            this.getTeleportPlayer().sendMessage(ChatUtil.fixColor("&8>> &7Teleportacja przebiegla pomyslnie!"));
            return;
        }
        final PlayerTeleportPacket teleportPacket = new PlayerTeleportPacket(this.getTeleportUser().getUUID(), this.teleportLocation.getBlockX(), this.teleportLocation.getBlockY(), this.teleportLocation.getBlockZ());
        this.getPlugin().getSectorClient().sendPacket(teleportPacket, this.sector);
        this.getPlugin().getSectorManager().sendToSector(this.getTeleportUser(), this.sector);
    }
}
