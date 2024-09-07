package pl.supereasy.sectors.api.packets.impl.guild;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class GuildUpgradePacket extends Packet {

    private String guildTag;
    private String tableName;
    private double value;

    public GuildUpgradePacket() {
    }

    public GuildUpgradePacket(String guildTag, String tableName, double value) {
        this.guildTag = guildTag;
        this.tableName = tableName;
        this.value = value;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getGuildTag() {
        return guildTag;
    }

    public String getTableName() {
        return tableName;
    }

    public double getValue() {
        return value;
    }
}
