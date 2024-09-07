package pl.supereasy.sectors.api.packets.impl.guild;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class GuildAllyRequestPacket extends Packet {

    private UUID requestedPlayer;
    private String guildToAccept;
    private String guildRequested;

    public GuildAllyRequestPacket() {
    }

    public GuildAllyRequestPacket(UUID requestedPlayer, String guildToAccept, String guildRequested) {
        this.requestedPlayer = requestedPlayer;
        this.guildToAccept = guildToAccept;
        this.guildRequested = guildRequested;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getRequestedPlayer() {
        return requestedPlayer;
    }

    public String getGuildToAccept() {
        return guildToAccept;
    }

    public String getGuildRequested() {
        return guildRequested;
    }
}
