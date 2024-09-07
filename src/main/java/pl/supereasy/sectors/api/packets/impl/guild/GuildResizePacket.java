package pl.supereasy.sectors.api.packets.impl.guild;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class GuildResizePacket extends Packet {

    private String guildTag;
    private String executorName;
    private int currentSize;

    public GuildResizePacket() {
    }

    public GuildResizePacket(String guildTag, String executorName, int currentSize) {
        this.guildTag = guildTag;
        this.executorName = executorName;
        this.currentSize = currentSize;
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

    public int getCurrentSize() {
        return currentSize;
    }
}
