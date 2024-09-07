package pl.supereasy.sectors.api.packets.impl.global;

import lombok.Data;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

@Data
public class SetTntHourPacket extends Packet {

    private String hourType;
    private int hour;

    public SetTntHourPacket(String hourType, int hour){
        this.hourType = hourType;
        this.hour = hour;
    }
    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
