package pl.supereasy.sectors.api.regions.impl;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.api.regions.api.Region;

public class RegionImpl implements Region {

    private transient World world = Bukkit.getWorld("world");
    private final int centerX;
    private final int centerZ;
    private int regionSize;
    private final Location centerLocation;

    public RegionImpl(int centerX, int centerZ, int regionSize) {
        this.centerX = centerX;
        this.centerZ = centerZ;
        this.regionSize = regionSize;
        this.centerLocation = new Location(this.world, this.centerX, 40, this.centerZ);
    }

    @Override
    public int getCenterX() {
        return this.centerX;
    }

    @Override
    public int getCenterZ() {
        return this.centerZ;
    }

    @Override
    public int getSize() {
        return this.regionSize;
    }

    @Override
    public void addSize(int value) {
        this.regionSize += value;
    }

    @Override
    public void setSize(int value) {
        this.regionSize = value;
    }

    @Override
    public Location getCenter() {
        return this.centerLocation;
    }

    @Override
    public boolean isCenter(Location loc) {
        return (loc.getY() >= 39) && (loc.getY() <= 43) && (loc.getX() <= this.getCenterX() + 2) && (loc.getX() >= this.getCenterX() - 2) && (loc.getZ() <= this.getCenterZ() + 2) && (loc.getZ() >= this.getCenterZ() - 2);
    }

    @Override
    public boolean isCenterWithAdd(Location loc, int add) {
        return (loc.getY() >= 39 - add) && (loc.getY() <= 43 + add) && (loc.getX() <= this.getCenterX() + 2 + add) && (loc.getX() >= this.getCenterX() - 2 - add) && (loc.getZ() <= this.getCenterZ() + 2 + add) && (loc.getZ() >= this.getCenterZ() - 2 - add);
    }

    @Override
    public boolean isInside(Location location) {
        if (this.world == null) {
            this.world = Bukkit.getWorld("world");
        }
        if (!location.getWorld().equals(this.world)) {
            return false;
        }
        int distancex = Math.abs(location.getBlockX() - this.centerX);
        int distancez = Math.abs(location.getBlockZ() - this.centerZ);
        return (distancex <= this.regionSize) && (distancez <= this.regionSize);
    }
}
