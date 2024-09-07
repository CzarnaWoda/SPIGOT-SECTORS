package pl.supereasy.sectors.api.packets.impl.events;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class GlobalEventPacket extends Packet {

    private String eventString;

    public GlobalEventPacket(){

    }

    public GlobalEventPacket(String eventString){
        this.eventString = eventString;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getEventString() {
        return eventString;
    }
}
