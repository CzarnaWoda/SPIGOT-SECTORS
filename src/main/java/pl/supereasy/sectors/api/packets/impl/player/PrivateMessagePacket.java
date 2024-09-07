package pl.supereasy.sectors.api.packets.impl.player;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class PrivateMessagePacket extends Packet {

    private String senderName;
    private String receiverName;
    private String message;

    public PrivateMessagePacket() {
    }

    public PrivateMessagePacket(String senderName, String receiverName, String message) {
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.message = message;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }


    public String getSenderName() {
        return senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getMessage() {
        return message;
    }
}
