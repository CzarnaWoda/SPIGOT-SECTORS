package pl.supereasy.sectors.api.packets.impl.user;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserKillPacket extends Packet {

    private UUID attackerUUID;
    private int attackAddPoints;
    private UUID victimUUID;
    private int victimRemovePoints;
    private String broadcastMessage;
    private Map<UUID, Integer> assistList = new HashMap<>();

    public UserKillPacket() {
    }

    public UserKillPacket(UUID attackerUUID, int attackAddPoints, UUID victimUUID, int victimRemovePoints, String broadcastMessage, Map<UUID, Integer> assistList) {
        this.attackerUUID = attackerUUID;
        this.attackAddPoints = attackAddPoints;
        this.victimUUID = victimUUID;
        this.victimRemovePoints = victimRemovePoints;
        this.broadcastMessage = broadcastMessage;
        this.assistList = assistList;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public Map<UUID, Integer> getAssistList() {
        return assistList;
    }

    public UUID getAttackerUUID() {
        return attackerUUID;
    }

    public int getAttackAddPoints() {
        return attackAddPoints;
    }

    public UUID getVictimUUID() {
        return victimUUID;
    }

    public int getVictimRemovePoints() {
        return victimRemovePoints;
    }

    public String getBroadcastMessage() {
        return broadcastMessage;
    }
}
