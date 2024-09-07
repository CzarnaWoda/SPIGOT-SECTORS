package pl.supereasy.sectors.api.packets.impl.player;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class GiveEventPacket extends Packet {

    private UUID targetUUID;
    private String eventString;

    public GiveEventPacket(){

    }

    public GiveEventPacket(UUID targetUUID, String eventString){
        this.targetUUID =targetUUID;
        this.eventString = eventString;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getEventString() {
        return eventString;
    }

    public UUID getTargetUUID() {
        return targetUUID;
    }
}
