package pl.supereasy.sectors.api.packets.impl.global;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class RegisterHomePacket extends Packet {

    private UUID userUUID;
    private String userName;
    private String homeName;
    private String sectorName;
    private int locX;
    private int locZ;
    private int locY;

    public RegisterHomePacket() {
    }

    public RegisterHomePacket(UUID userUUID, String userName, String homeName, String sectorName, int locX, int locY, int locZ) {
        this.userUUID = userUUID;
        this.userName = userName;
        this.homeName = homeName;
        this.sectorName = sectorName;
        this.locX = locX;
        this.locZ = locZ;
        this.locY = locY;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getUserUUID() {
        return userUUID;
    }

    public String getHomeName() {
        return homeName;
    }

    public int getLocX() {
        return locX;
    }

    public int getLocZ() {
        return locZ;
    }

    public int getLocY() {
        return locY;
    }

    public String getUserName() {
        return userName;
    }

    public String getSectorName() {
        return sectorName;
    }
}
