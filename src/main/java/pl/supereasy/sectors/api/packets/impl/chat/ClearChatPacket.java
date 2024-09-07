package pl.supereasy.sectors.api.packets.impl.chat;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class ClearChatPacket extends Packet {

    private int count;

    public ClearChatPacket(){

    }
    public ClearChatPacket(int count){
        this.count = count;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public int getCount() {
        return count;
    }
}
