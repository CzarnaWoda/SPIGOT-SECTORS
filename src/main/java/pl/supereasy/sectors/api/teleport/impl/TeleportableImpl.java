package pl.supereasy.sectors.api.teleport.impl;

import org.bukkit.Location;
import pl.supereasy.sectors.api.teleport.api.Teleportable;

public class TeleportableImpl implements Teleportable {

    private final String locationName;
    private final Location teleportLocation;


    public TeleportableImpl(String locationName, Location teleportLocation) {
        this.locationName = locationName;
        this.teleportLocation = teleportLocation;
    }

    @Override
    public Location getLocation() {
        return this.teleportLocation;
    }

    @Override
    public String getName() {
        return this.locationName;
    }
}
