package pl.supereasy.sectors.api.teleport.impl;

import org.bukkit.Bukkit;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.player.PlayerRandomTeleportPacket;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.api.teleport.api.TeleportRequest;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.TeleportUtil;

public class RandomLocationTeleport extends TeleportRequest {

    private final Sector sector;

    public RandomLocationTeleport(SectorPlugin plugin, User teleportUser, Sector sector) {
        super(plugin, teleportUser);
        this.sector = sector;
    }

    @Override
    public void reuqestAccepted() {
        if (this.sector.getUniqueSectorID() == this.getPlugin().getCurrentSectorConfig().getSectorUniqueID()) {
            TeleportUtil.randomTeleport(getTeleportPlayer(), sector.getMinX() + 100, sector.getMaxX() - 100, sector.getMinZ() + 100, sector.getMaxZ() - 100);
            return;
        }
        final Packet packet = new PlayerRandomTeleportPacket(getTeleportUser().getUUID());
        this.getPlugin().getSectorClient().sendPacket(packet, sector);
        this.getPlugin().getSectorManager().sendToSector(this.getTeleportUser(), sector);
    }
}
