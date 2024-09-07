package pl.supereasy.sectors.api.packets.impl.guild;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class AlliancePermissionChange extends Packet {

    private UUID allianceID;
    private UUID playerID;
    private String guildTag;
    private boolean permissionStatus;

    public AlliancePermissionChange() {
    }

    public AlliancePermissionChange(UUID allianceID, UUID playerID, String guildTag, boolean permissionStatus) {
        this.allianceID = allianceID;
        this.playerID = playerID;
        this.guildTag = guildTag;
        this.permissionStatus = permissionStatus;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public String getGuildTag() {
        return guildTag;
    }

    public UUID getAllianceID() {
        return allianceID;
    }

    public boolean isPermissionStatus() {
        return permissionStatus;
    }
}
