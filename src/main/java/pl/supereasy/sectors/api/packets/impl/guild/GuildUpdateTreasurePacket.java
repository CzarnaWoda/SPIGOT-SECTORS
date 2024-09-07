package pl.supereasy.sectors.api.packets.impl.guild;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class GuildUpdateTreasurePacket extends Packet {

    private String guildTag;
    private String treasureItems;

    public GuildUpdateTreasurePacket() {
    }

    public GuildUpdateTreasurePacket(String guildTag, String treasureItems) {
        this.guildTag = guildTag;
        this.treasureItems = treasureItems;
    }

    @Override
    public void handlePacket(PacketHandler handler) {

    }

    public String getGuildTag() {
        return guildTag;
    }

    public String getTreasureItems() {
        return treasureItems;
    }
}
