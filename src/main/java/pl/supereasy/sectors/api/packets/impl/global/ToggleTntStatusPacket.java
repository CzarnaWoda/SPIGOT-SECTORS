package pl.supereasy.sectors.api.packets.impl.global;

import lombok.Data;
import lombok.NonNull;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

@Data
public class ToggleTntStatusPacket extends Packet {

    @NonNull private boolean enable;
    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
