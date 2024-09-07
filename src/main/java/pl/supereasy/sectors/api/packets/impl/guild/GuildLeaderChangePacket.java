package pl.supereasy.sectors.api.packets.impl.guild;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class GuildLeaderChangePacket extends Packet {

    private String guildTag;
    private UUID oldLeader;
    private UUID newLeader;
    private String newLeaderName;

    public GuildLeaderChangePacket() {
    }

    public GuildLeaderChangePacket(String guildTag, UUID oldLeader, UUID newLeader, String newLeaderName) {
        this.guildTag = guildTag;
        this.oldLeader = oldLeader;
        this.newLeader = newLeader;
        this.newLeaderName = newLeaderName;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getGuildTag() {
        return guildTag;
    }

    public UUID getOldLeader() {
        return oldLeader;
    }

    public UUID getNewLeader() {
        return newLeader;
    }

    public String getNewLeaderName() {
        return newLeaderName;
    }
}
