package pl.supereasy.sectors.api.packets.impl.guild;

import io.netty.buffer.ByteBuf;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class GuildBreakHeartPacket extends Packet {

    private String targetGuild;


    public GuildBreakHeartPacket(String targetGuild) {
        this.targetGuild = targetGuild;
    }

    public GuildBreakHeartPacket() {
    }


    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
