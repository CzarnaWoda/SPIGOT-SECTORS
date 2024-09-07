package pl.supereasy.sectors.api.packets.impl.configs;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class SlowdownPacket extends Packet {

    private int seconds;
    public SlowdownPacket(int seconds){
        this.seconds = seconds;
    }
    public SlowdownPacket(){

    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public int getSeconds() {
        return seconds;
    }
}
