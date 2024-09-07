package pl.supereasy.sectors.api.sectors.data;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.redisson.api.RSet;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.sectors.type.SectorType;
import pl.supereasy.sectors.guilds.area.GuildArea;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class Sector {

    private final String sectorName;
    private final int minX, minZ, maxZ, maxX, offset, uniqueSectorID;
    private World world;
    private Location lowest, highest;
    private int wallSize;
    private final SectorType sectorType;
    //private final Set<String> onlinePlayers;
    private final RSet<String> onlinePlayers;
    private final Set<GuildArea> guildAreas;
    private double tps;
    private Long lastUpdate;
    private boolean isBroadcaster;

    public Sector(final String sectorName, int maxX, int maxZ, int minX, int minZ, int offset, SectorType sectorType, int uniqueSectorID) {
        this.sectorName = sectorName;
        this.maxX = maxX;
        this.maxZ = maxZ;
        this.minX = minX;
        this.minZ = minZ;
        this.offset = offset;
        this.uniqueSectorID = uniqueSectorID;
        this.onlinePlayers = SectorPlugin.getInstance().getRedissonClient().getSet(sectorName + "-online");
        this.guildAreas = new HashSet<>();
        this.sectorType = sectorType;
        initializePosition();
        initializeWallSize();
        this.tps = 20;
        this.lastUpdate = System.currentTimeMillis();
        if (this.sectorType == SectorType.SPAWN && this.sectorName.equalsIgnoreCase("spawn-1")) {
            this.isBroadcaster = true;
        }
        SectorPlugin.getInstance().getLogger().log(Level.INFO, "Zaladowano sektor " + sectorName);
    }
    public void initializePosition(){
        Vector lower = new Vector(minX, 0, minZ);
        Vector higher = new Vector(maxX, 256, maxZ);

        this.lowest = lower.toLocation(world);
        this.highest = higher.toLocation(world);
    }
    public void initializeWallSize(){
        this.wallSize = Math.max(maxX - minX, maxZ - minZ);
    }

    public boolean entityInSector(Location location) {
        return !(this.lowest == null || this.highest == null || location == null)
                && (location.getBlockX() > this.lowest.getBlockX())
                && (location.getBlockX() < this.highest.getBlockX())
                && (location.getBlockY() > this.lowest.getBlockY())
                && (location.getBlockY() < this.highest.getBlockY())
                && (location.getBlockZ() > this.lowest.getBlockZ())
                && (location.getBlockZ() < this.highest.getBlockZ());
    }
    public boolean entityInSectorWithOffSet(Location location) {
        return !(this.lowest == null || this.highest == null || location == null)
                && (location.getBlockX() - ((offset/2)-2) > this.lowest.getBlockX())
                && (location.getBlockX() + ((offset/2)-2) < this.highest.getBlockX())
                && (location.getBlockY() - ((offset/2)-2) > this.lowest.getBlockY())
                && (location.getBlockY() + ((offset/2)-2) < this.highest.getBlockY())
                && (location.getBlockZ() - ((offset/2)-2) > this.lowest.getBlockZ())
                && (location.getBlockZ()  + ((offset/2)-2) < this.highest.getBlockZ());
    }
    public boolean isSectorOnline() {
        return System.currentTimeMillis() - this.lastUpdate < 5000;
    }

    public boolean smthInSector(int x, int z) {
        return entityInSector(new Location(world, x, 100, z));
    }

    public Location getHighest() {
        return highest;
    }

    public Location getLowest() {
        return lowest;
    }

    public int getUniqueSectorID() {
        return uniqueSectorID;
    }

    public boolean isNearBorder(Location location, int additional) {
        return !entityInSector(location) && howCloseBorder(location) <= additional;
    }
    public double howCloseBorder(Location location) {
        if (entityInSector(location))
            return Double.MAX_VALUE;

        double x = location.getX();
        double z = location.getZ();
        double distanceWest = Math.abs(minX - x);
        double distanceEast = Math.abs(maxX - x);
        double distanceNorth = Math.abs(minZ - z);
        double distanceSouth = Math.abs(maxZ - z);
        double distanceX = Math.min(distanceWest, distanceEast);
        double distanceZ = Math.min(distanceNorth, distanceSouth);
        double distance = Math.min(distanceX, distanceZ);
        return distance + offset;
    }
    public double howCloseBorderInSector(Location location) {
        double x = location.getX();
        double z = location.getZ();
        double distanceWest = Math.abs(minX - x);
        double distanceEast = Math.abs(maxX - x);
        double distanceNorth = Math.abs(minZ - z);
        double distanceSouth = Math.abs(maxZ - z);
        double distanceX = Math.min(distanceWest, distanceEast);
        double distanceZ = Math.min(distanceNorth, distanceSouth);
        double distance = Math.min(distanceX, distanceZ);
        return distance;
    }
    public double getDistanceWest(Location location){
        double x = location.getX();

        return Math.abs(minX - x);
    }
    public double getDistanceEast(Location location){
        double x = location.getX();

        return Math.abs(maxX - x);
    }
    public double getDistanceNorth(Location location){
        double z = location.getZ();

        return Math.abs(minZ - z);
    }

    public double getDistanceSouth(Location location) {
        double z = location.getZ();

        return Math.abs(maxZ - z);
    }

    public double getTps() {
        return tps;
    }

    public void setTps(double tps) {
        this.tps = tps;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Set<GuildArea> getGuildAreas() {
        return guildAreas;
    }

    public String getSectorName() {
        return sectorName;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getOffset() {
        return offset;
    }

    public int getWallSize() {
        return wallSize;
    }

    public World getWorld() {
        return world;
    }

    public SectorType getSectorType() {
        return sectorType;
    }

    public boolean isBroadcaster() {
        return isBroadcaster;
    }

    public void addOnlinePlayer(final String name) {
        this.onlinePlayers.addAsync(name);
    }

    public void removeOnlinePlayer(final String name) {
        this.onlinePlayers.removeAsync(name);
    }

    public boolean isOnline(final String playerName) {
        return this.onlinePlayers.contains(playerName);
    }

    public Set<String> getOnlinePlayers() {
        return onlinePlayers;
    }

}
