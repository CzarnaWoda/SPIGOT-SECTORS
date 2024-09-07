package pl.supereasy.sectors.api.packets.impl.guild;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class GuildAllianceMessagePacket extends Packet {

    private String guildTag;
    private String message;

    public GuildAllianceMessagePacket(String guildTag, String message) {
        this.guildTag = guildTag;
        this.message = message;
    }

    public GuildAllianceMessagePacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }


    public String getMessage() {
        return message;
    }

    public String getGuildTag() {
        return guildTag;
    }
}
