package pl.supereasy.sectors.api.packets.impl.chat;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class MessageToUserPacket extends Packet {

    private UUID target;
    private String message;

    public MessageToUserPacket() {
    }

    public MessageToUserPacket(UUID target, String message) {
        this.target = target;
        this.message = message;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getTarget() {
        return target;
    }

    public String getMessage() {
        return message;
    }
}
