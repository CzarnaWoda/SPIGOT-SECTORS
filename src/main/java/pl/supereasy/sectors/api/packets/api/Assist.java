package pl.supereasy.sectors.api.packets.api;

import java.util.UUID;

public class Assist {

    private final UUID uuid;
    private final int assistPoints;

    public Assist(UUID uuid, int assistPoints) {
        this.uuid = uuid;
        this.assistPoints = assistPoints;
    }


    public UUID getUuid() {
        return uuid;
    }

    public int getAssistPoints() {
        return assistPoints;
    }
}
