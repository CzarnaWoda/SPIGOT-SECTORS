package pl.supereasy.sectors.api.packets.impl.guild;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class GuildAllyRemovePacket extends Packet {

    private String firstGuildTag;
    private String secondGuildTag;

    public GuildAllyRemovePacket() {
    }

    public GuildAllyRemovePacket(String firstGuildTag, String secondGuildTag) {
        this.firstGuildTag = firstGuildTag;
        this.secondGuildTag = secondGuildTag;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getFirstGuildTag() {
        return firstGuildTag;
    }

    public String getSecondGuildTag() {
        return secondGuildTag;
    }
}