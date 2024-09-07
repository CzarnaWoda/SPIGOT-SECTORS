package pl.supereasy.sectors.api.packets.impl.user;

import pl.supereasy.sectors.api.packets.api.Assist;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.List;

public class UpdateAssistsPacket extends Packet {

    private List<Assist> assists;

    public UpdateAssistsPacket(List<Assist> assists) {
        this.assists = assists;
    }

    public UpdateAssistsPacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
