package pl.supereasy.sectors.api.packets.impl.configs;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class ToggleManagerPacket extends Packet {
    private boolean status;
    private String path;

    public ToggleManagerPacket(boolean status,String path){
        this.status = status;
        this.path = path;
    }
    public ToggleManagerPacket(){

    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getPath() {
        return path;
    }

    public boolean isStatus() {
        return status;
    }
}
