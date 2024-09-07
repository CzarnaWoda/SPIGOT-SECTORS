package pl.supereasy.sectors.api.packets.impl.guild;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class GuildRemovePacket extends Packet {

    private String guildTag;
    private String executorName;

    public GuildRemovePacket() {
    }

    public GuildRemovePacket(String guildTag, String executorName) {
        this.guildTag = guildTag;
        this.executorName = executorName;
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
}
