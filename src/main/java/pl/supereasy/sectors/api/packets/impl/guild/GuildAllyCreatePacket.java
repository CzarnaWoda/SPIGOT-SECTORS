package pl.supereasy.sectors.api.packets.impl.guild;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class GuildAllyCreatePacket extends Packet {

    private UUID allianceID;
    private String firstGuildTag;
    private String secondGuildTag;

    public GuildAllyCreatePacket() {
    }

    public GuildAllyCreatePacket(UUID allianceID, String firstGuildTag, String secondGuildTag) {
        this.allianceID = allianceID;
        this.firstGuildTag = firstGuildTag;
        this.secondGuildTag = secondGuildTag;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }


    public UUID getAllianceID() {
        return allianceID;
    }

    public String getFirstGuildTag() {
        return firstGuildTag;
    }

    public String getSecondGuildTag() {
        return secondGuildTag;
    }
}
