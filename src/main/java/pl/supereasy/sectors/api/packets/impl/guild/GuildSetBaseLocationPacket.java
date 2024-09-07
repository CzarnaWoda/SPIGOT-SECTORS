package pl.supereasy.sectors.api.packets.impl.guild;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class GuildSetBaseLocationPacket extends Packet {

    private String executorName;
    private String guildTag;
    private int guildBaseX;
    private int guildBaseY;
    private int guildBaseZ;

    public GuildSetBaseLocationPacket() {
    }

    public GuildSetBaseLocationPacket(String executorName, String guildTag, int guildBaseX, int guildBaseY, int guildBaseZ) {
        this.executorName = executorName;
        this.guildTag = guildTag;
        this.guildBaseX = guildBaseX;
        this.guildBaseY = guildBaseY;
        this.guildBaseZ = guildBaseZ;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getExecutorName() {
        return executorName;
    }

    public String getGuildTag() {
        return guildTag;
    }

    public int getGuildBaseX() {
        return guildBaseX;
    }

    public int getGuildBaseY() {
        return guildBaseY;
    }

    public int getGuildBaseZ() {
        return guildBaseZ;
    }
}
