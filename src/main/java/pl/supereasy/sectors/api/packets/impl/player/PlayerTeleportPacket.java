package pl.supereasy.sectors.api.packets.impl.player;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class PlayerTeleportPacket extends Packet {

    private UUID teleportPlayer;
    private int locX;
    private int locY;
    private int locZ;

    public PlayerTeleportPacket() {
    }

    public PlayerTeleportPacket(final UUID teleportPlayer, int locX, int locY, int locZ) {
        this.teleportPlayer = teleportPlayer;
        this.locX = locX;
        this.locY = locY;
        this.locZ = locZ;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getTeleportPlayer() {
        return teleportPlayer;
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
}
