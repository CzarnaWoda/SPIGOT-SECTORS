package pl.supereasy.sectors.api.packets.impl.guild;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class WarUpdateValuesPacket extends Packet {

    private final String firstGuildTag;
    private final int firstGuildKills;
    private final int firstGuildDeaths;
    private final String secondGuildTag;
    private final int secondGuildKills;
    private final int secondGuildDeaths;


    public WarUpdateValuesPacket(String firstGuildTag, int firstGuildKills, int firstGuildDeaths, String secondGuildTag, int secondGuildKills, int secondGuildDeaths) {
        this.firstGuildTag = firstGuildTag;
        this.firstGuildKills = firstGuildKills;
        this.firstGuildDeaths = firstGuildDeaths;
        this.secondGuildTag = secondGuildTag;
        this.secondGuildKills = secondGuildKills;
        this.secondGuildDeaths = secondGuildDeaths;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getFirstGuildTag() {
        return firstGuildTag;
    }

    public int getFirstGuildKills() {
        return firstGuildKills;
    }

    public int getFirstGuildDeaths() {
        return firstGuildDeaths;
    }

    public String getSecondGuildTag() {
        return secondGuildTag;
    }

    public int getSecondGuildKills() {
        return secondGuildKills;
    }

    public int getSecondGuildDeaths() {
        return secondGuildDeaths;
    }
}
