package pl.supereasy.sectors.core.home.impl;

import org.bukkit.Location;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.core.home.api.Home;

public class HomeImpl implements Home {

    private final String homeName;
    private final Location homeLocation;
    private final Sector sector;

    public HomeImpl(String homeName, Location homeLocation, Sector sector) {
        this.homeName = homeName;
        this.homeLocation = homeLocation;
        this.sector = sector;
    }

    @Override
    public Sector getSector() {
        return this.sector;
    }

    @Override
    public Location getLocation() {
        return this.homeLocation;
    }

    @Override
    public String getName() {
        return this.homeName;
    }
}
