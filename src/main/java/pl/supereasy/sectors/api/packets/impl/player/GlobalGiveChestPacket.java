package pl.supereasy.sectors.api.packets.impl.player;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class GlobalGiveChestPacket extends Packet {

    private final int amount;

    public GlobalGiveChestPacket(int amount){
        this.amount = amount;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public int getAmount() {
        return amount;
    }
}
