package pl.supereasy.sectors.api.packets.impl.user;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class UserStatUpdate extends Packet {

    private UUID uuid;
    private String tableName;
    private int newValue;

    public UserStatUpdate() {
    }


    public UserStatUpdate(UUID uuid, String tableName, int newValue) {
        this.uuid = uuid;
        this.tableName = tableName;
        this.newValue = newValue;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getTableName() {
        return tableName;
    }

    public int getNewValue() {
        return newValue;
    }
}
