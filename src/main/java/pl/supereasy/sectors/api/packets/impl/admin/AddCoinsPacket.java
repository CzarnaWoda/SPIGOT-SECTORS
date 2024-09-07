package pl.supereasy.sectors.api.packets.impl.admin;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class AddCoinsPacket extends Packet {

    private UUID targetUUID;
    private int coinsAmount;
    private String executorName;

    public AddCoinsPacket() {
    }

    public AddCoinsPacket(UUID targetUUID, int coinsAmount, String executorName) {
        this.targetUUID = targetUUID;
        this.coinsAmount = coinsAmount;
        this.executorName = executorName;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getTargetUUID() {
        return targetUUID;
    }

    public int getCoinsAmount() {
        return coinsAmount;
    }

    public String getExecutorName() {
        return executorName;
    }
}
