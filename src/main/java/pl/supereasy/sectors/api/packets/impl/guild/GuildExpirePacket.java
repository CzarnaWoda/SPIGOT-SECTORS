package pl.supereasy.sectors.api.packets.impl.guild;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class GuildExpirePacket extends Packet {

    private String tag;
    private int guildX;
    private int guildZ;

    public GuildExpirePacket() {
    }

    public GuildExpirePacket(String tag, int guildX, int guildZ) {
        this.tag = tag;
        this.guildX = guildX;
        this.guildZ = guildZ;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getTag() {
        return tag;
    }

    public int getGuildX() {
        return guildX;
    }

    public int getGuildZ() {
        return guildZ;
    }
}
