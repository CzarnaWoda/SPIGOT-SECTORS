package pl.supereasy.sectors.api.packets.impl.configs;

import lombok.Data;
import lombok.NonNull;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

@Data
public class EnableEnchantPacket extends Packet {

    @NonNull private long time;

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
