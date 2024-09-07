package pl.supereasy.sectors.api.teleport.impl;

import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.impl.player.PlayerToPlayerTeleportPacket;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.api.teleport.api.TeleportRequest;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.TimeUtil;

import java.util.UUID;

public class PlayerTeleport extends TeleportRequest {

    private final User targetUser;

    public PlayerTeleport(SectorPlugin plugin, User teleportUser, User targetUser) {
        super(plugin, teleportUser);
        this.targetUser = targetUser;
    }

    @Override
    public void reuqestAccepted() {
        getTeleportUser().setProtection(TimeUtil.parseDateDiff("5s",true));
        if (this.targetUser.getSector().getUniqueSectorID() == this.getPlugin().getCurrentSectorConfig().getSectorUniqueID()) {
            this.getTeleportPlayer().teleport(this.targetUser.asPlayer());
            this.getTeleportUser().setUserLocation(this.targetUser.asPlayer().getLocation());
            this.getTeleportPlayer().sendMessage(ChatUtil.fixColor("&8>> &7Teleportacja przebiegla pomyslnie!"));
            return;
        }
        final PlayerToPlayerTeleportPacket teleportPacket = new PlayerToPlayerTeleportPacket(this.getTeleportPlayer().getUniqueId(), this.targetUser.getUUID());
        this.getPlugin().getSectorClient().sendPacket(teleportPacket, this.targetUser.getSector());
        this.getPlugin().getSectorManager().sendToSector(this.getTeleportUser(), this.targetUser.getSector());
    }
}
