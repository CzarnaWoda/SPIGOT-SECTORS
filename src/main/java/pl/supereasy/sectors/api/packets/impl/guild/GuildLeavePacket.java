package pl.supereasy.sectors.api.packets.impl.guild;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class GuildLeavePacket extends Packet {

    private UUID playerUUID;
    private String playerName;
    private String guildTag;

    public GuildLeavePacket() {
    }


    public GuildLeavePacket(UUID playerUUID, String playerName, String guildTag) {
        this.playerUUID = playerUUID;
        this.playerName = playerName;
        this.guildTag = guildTag;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getGuildTag() {
        return guildTag;
    }
}
