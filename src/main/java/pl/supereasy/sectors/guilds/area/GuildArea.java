package pl.supereasy.sectors.guilds.area;

import java.io.Serializable;

public class GuildArea implements Serializable {

    private final String sectorName;
    private final int locX;
    private final int locZ;

    public GuildArea(String sectorName, int locX, int locZ) {
        this.sectorName = sectorName;
        this.locX = locX;
        this.locZ = locZ;
    }

    public String getSectorName() {
        return sectorName;
    }

    public int getLocX() {
        return locX;
    }

    public int getLocZ() {
        return locZ;
    }
}
