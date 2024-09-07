package pl.supereasy.sectors.api.teleport.impl;

import org.bukkit.Bukkit;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.player.PlayerSpawnTeleportPacket;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.api.teleport.api.TeleportRequest;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;

public class SpawnTeleport extends TeleportRequest {

    private final Sector spawnSector;

    public SpawnTeleport(SectorPlugin plugin, User teleportUser, Sector spawnSector) {
        super(plugin, teleportUser);
        this.spawnSector = spawnSector;
    }

    @Override
    public void reuqestAccepted() {
        if (this.getTeleportUser().getSector().getUniqueSectorID() == spawnSector.getUniqueSectorID()) {
            this.getTeleportPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation());
            this.getTeleportUser().setUserLocation(Bukkit.getWorld("world").getSpawnLocation());
            this.getTeleportPlayer().sendMessage(ChatUtil.fixColor("&8>> &7Teleportacja przebiegla pomyslnie!"));
            return;
        }
        final Packet packet = new PlayerSpawnTeleportPacket(getTeleportPlayer().getUniqueId());
        getPlugin().getSectorClient().sendPacket(packet, spawnSector);
        getPlugin().getSectorManager().sendToSector(getTeleportUser(), spawnSector);
    }
}
