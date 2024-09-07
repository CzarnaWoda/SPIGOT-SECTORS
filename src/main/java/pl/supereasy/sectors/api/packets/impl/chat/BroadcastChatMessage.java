package pl.supereasy.sectors.api.packets.impl.chat;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class BroadcastChatMessage extends Packet {

    private String chatMessage;

    public BroadcastChatMessage() {
    }

    public BroadcastChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }


    public String getChatMessage() {
        return chatMessage;
    }

}