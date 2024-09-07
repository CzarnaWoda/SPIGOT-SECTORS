package pl.supereasy.sectors.api.packets.impl.global;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;


public class RegisterWarpPacket extends Packet {

    private String warpName;
    private String sectorName;
    private String createdBy;
    private int locX;
    private int locY;
    private int locZ;

    public RegisterWarpPacket() {
    }

    public RegisterWarpPacket(String warpName, String sectorName, String createdBy, int locX, int locY, int locZ) {
        this.warpName = warpName;
        this.sectorName = sectorName;
        this.createdBy = createdBy;
        this.locX = locX;
        this.locY = locY;
        this.locZ = locZ;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getWarpName() {
        return warpName;
    }

    public int getLocX() {
        return locX;
    }

    public int getLocY() {
        return locY;
    }

    public int getLocZ() {
        return locZ;
    }

    public String getSectorName() {
        return sectorName;
    }

    public String getCreatedBy() {
        return createdBy;
    }
}
