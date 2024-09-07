package pl.supereasy.sectors.api.packets.impl.chat;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class SpecifiedMessageTypePacket extends Packet {

    private String message;
    private String messageType;

    public SpecifiedMessageTypePacket(String message, String messageType) {
        this.message = message;
        this.messageType = messageType;
    }

    public SpecifiedMessageTypePacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getMessage() {
        return message;
    }

    public String getMessageType() {
        return messageType;
    }
}
