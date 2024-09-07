package pl.supereasy.sectors.api.packets.impl.guild;

import lombok.Data;
import lombok.NonNull;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

@Data
public class GuildCreateCollectPacket extends Packet {

    @NonNull private String executor;
    @NonNull private String guildTag;
    @NonNull private int amount;
    @NonNull private String material;

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
