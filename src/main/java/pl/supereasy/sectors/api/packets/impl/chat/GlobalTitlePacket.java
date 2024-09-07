package pl.supereasy.sectors.api.packets.impl.chat;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class GlobalTitlePacket extends Packet {

    private String title;
    private String subTitle;
    public GlobalTitlePacket(String title, String subTitle){
        this.title = title;
        this.subTitle = subTitle;
    }
    public GlobalTitlePacket(){

    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getTitle() {
        return title;
    }
}
