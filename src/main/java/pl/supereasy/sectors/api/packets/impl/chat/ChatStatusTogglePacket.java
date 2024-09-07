package pl.supereasy.sectors.api.packets.impl.chat;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class ChatStatusTogglePacket extends Packet {
    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
