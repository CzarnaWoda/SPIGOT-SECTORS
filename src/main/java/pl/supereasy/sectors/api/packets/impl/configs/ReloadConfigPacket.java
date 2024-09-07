package pl.supereasy.sectors.api.packets.impl.configs;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class ReloadConfigPacket extends Packet {

    private String config;

    public ReloadConfigPacket(String config){
        this.config = config;
    }
    public ReloadConfigPacket(){

    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getConfig() {
        return config;
    }
}
