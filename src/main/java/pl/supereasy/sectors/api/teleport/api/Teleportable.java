package pl.supereasy.sectors.api.teleport.api;

import org.bukkit.Location;
import pl.supereasy.sectors.util.api.Nameable;

public interface Teleportable extends Nameable {

    Location getLocation();

}
