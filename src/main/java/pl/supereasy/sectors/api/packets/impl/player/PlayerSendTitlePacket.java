package pl.supereasy.sectors.api.packets.impl.player;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class PlayerSendTitlePacket extends Packet {

    private String target;
    private String title;
    private String subtitle;


    public PlayerSendTitlePacket(){

    }

    public PlayerSendTitlePacket(String target,String title, String subtitle){
        this.target = target;
        this.title = title;
        this.subtitle = subtitle;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getTarget() {
        return target;
    }
}
