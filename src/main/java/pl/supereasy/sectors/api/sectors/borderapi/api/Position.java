package pl.supereasy.sectors.api.sectors.borderapi.api;

import org.bukkit.Location;

public class Position {

    private double x;
    private double z;

    public Position(Location location){
        this(location.getX(),location.getZ());
    }

    public Position(double x, double z) {
        this.x = x;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
