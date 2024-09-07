package pl.supereasy.sectors.api.packets.impl.tops;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;
import pl.supereasy.sectors.core.tops.enums.TopType;
import pl.supereasy.sectors.core.tops.impl.TopList;

import java.io.Serializable;

public class TopPacket extends Packet {

    private final TopType topType;
    private final TopList topList;

    public TopPacket(TopType topType, TopList topList) {
        this.topType = topType;
        this.topList = topList;
    }

    public TopType getTopType() {
        return topType;
    }

    public TopList getTopList() {
        return topList;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
