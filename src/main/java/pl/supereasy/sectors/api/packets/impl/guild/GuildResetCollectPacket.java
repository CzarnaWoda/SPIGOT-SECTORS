package pl.supereasy.sectors.api.packets.impl.guild;

import lombok.Data;
import lombok.NonNull;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

@Data
public class GuildResetCollectPacket extends Packet {

    @NonNull private String guildTag;

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
