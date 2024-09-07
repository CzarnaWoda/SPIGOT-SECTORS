package pl.supereasy.sectors.api.packets.impl.chat;

import lombok.Data;
import lombok.NonNull;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

@Data
public class BossBarSendPacket extends Packet {

    @NonNull private String text;

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
