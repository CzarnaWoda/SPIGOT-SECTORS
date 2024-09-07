package pl.supereasy.sectors.api.packets.api;

import pl.supereasy.sectors.api.netty.enums.SourceType;

public class SourcePacket {

    private final SourceType type;
    private String sourceSectorName;
    private final Packet packet;

    public SourcePacket(SourceType type, String sourceSectorName, Packet packet) {
        this.type = type;
        this.sourceSectorName = sourceSectorName;
        this.packet = packet;
    }

    public SourcePacket(SourceType type, Packet packet) {
        this.type = type;
        this.packet = packet;
    }

    public String getSourceSectorName() {
        return sourceSectorName;
    }

    public SourceType getType() {
        return type;
    }

    public Packet getPacket() {
        return packet;
    }
}
