package pl.supereasy.sectors.api.packets.impl.guild;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class GuildRemoveCoinsPacket extends Packet {

    private String guildTag;
    private String executorName;
    private int coinsAmount;

    public GuildRemoveCoinsPacket() {
    }

    public GuildRemoveCoinsPacket(String guildTag, String executorName, int coinsAmount) {
        this.guildTag = guildTag;
        this.executorName = executorName;
        this.coinsAmount = coinsAmount;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getGuildTag() {
        return guildTag;
    }

    public String getExecutorName() {
        return executorName;
    }

    public int getCoinsAmount() {
        return coinsAmount;
    }
}