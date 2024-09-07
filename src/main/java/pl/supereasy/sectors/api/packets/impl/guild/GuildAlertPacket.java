package pl.supereasy.sectors.api.packets.impl.guild;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class GuildAlertPacket extends Packet {

    private final String message;
    private final String guildTag;


    public GuildAlertPacket(String message, String guildTag) {
        this.message = message;
        this.guildTag = guildTag;
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
