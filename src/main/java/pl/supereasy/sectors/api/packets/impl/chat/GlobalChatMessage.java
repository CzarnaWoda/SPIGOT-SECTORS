package pl.supereasy.sectors.api.packets.impl.chat;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class GlobalChatMessage extends Packet {

    private UUID senderUUID;
    private String chatMessage;

    public GlobalChatMessage() {
    }

    public GlobalChatMessage(UUID senderUUID, String chatMessage) {
        this.senderUUID = senderUUID;
        this.chatMessage = chatMessage;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getSenderUUID() {
        return senderUUID;
    }

    public String getChatMessage() {
        return chatMessage;
    }

}

