package pl.supereasy.sectors.api.packets.impl.guild;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class GuildSetPvPPacket extends Packet {

    private String guildTag;
    private String executorName;
    private boolean isPvP;

    public GuildSetPvPPacket() {
    }

    public GuildSetPvPPacket(String guildTag, String executorName, boolean isPvP) {
        this.guildTag = guildTag;
        this.executorName = executorName;
        this.isPvP = isPvP;
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

    public boolean isPvP() {
        return isPvP;
    }
}
