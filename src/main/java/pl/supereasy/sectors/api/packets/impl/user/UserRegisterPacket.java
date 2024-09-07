package pl.supereasy.sectors.api.packets.impl.user;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class UserRegisterPacket extends Packet {

    private UUID uuid;
    private String userName;
    private String sectorName;
    private int sectorID;

    public UserRegisterPacket() {
    }

    public UserRegisterPacket(UUID uuid, String userName, String sectorName, int sectorID) {
        this.uuid = uuid;
        this.userName = userName;
        this.sectorName = sectorName;
        this.sectorID = sectorID;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getUserName() {
        return userName;
    }

    public int getSectorID() {
        return sectorID;
    }

    public String getSectorName() {
        return sectorName;
    }
}
