package pl.supereasy.sectors.api.packets.impl.user;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class UserUpdatePacket extends Packet {

    private UUID userUUID;
    private String userName;
    private String userNBT;
    private int lastSectorID;

    public UserUpdatePacket() {
    }

    public UserUpdatePacket(UUID userUUID, String userName, String userNBT, int lastSectorID) {
        this.userUUID = userUUID;
        this.userName = userName;
        this.userNBT = userNBT;
        this.lastSectorID = lastSectorID;
    }

    @Override
    public void handlePacket(PacketHandler handler) {

    }

    public UUID getUserUUID() {
        return userUUID;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserNBT() {
        return userNBT;
    }

    public int getLastSectorID() {
        return lastSectorID;
    }
}
