package pl.supereasy.sectors.api.packets.impl.configs;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class KitstatusPacket extends Packet {

    private String kitKey;
    private boolean status;
    public KitstatusPacket(String kitKey, boolean status){
        this.kitKey = kitKey;
        this.status = status;
    }
    public KitstatusPacket(){

    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getKitKey() {
        return kitKey;
    }

    public boolean isStatus() {
        return status;
    }
}
