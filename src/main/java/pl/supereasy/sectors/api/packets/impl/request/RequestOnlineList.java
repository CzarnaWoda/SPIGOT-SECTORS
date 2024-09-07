package pl.supereasy.sectors.api.packets.impl.request;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestOnlineList extends Packet {

    private int sectorsSize;
    private final Map<String, List<String>> onlineMap = new HashMap<>();

    public RequestOnlineList() {
    }


    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public int getSectorsSize() {
        return sectorsSize;
    }

    public Map<String, List<String>> getOnlineMap() {
        return onlineMap;
    }
}
