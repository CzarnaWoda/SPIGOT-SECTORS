package pl.supereasy.sectors.api.regions.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface Region {

    int getCenterX();

    int getCenterZ();

    int getSize();

    void addSize(final int value);

    void setSize(final int value);

    Location getCenter();

    boolean isCenter(final Location location);

    boolean isCenterWithAdd(final Location location, int add);

    default boolean isInside(final Player player) {
        return isInside(player.getLocation());
    }

    boolean isInside(final Location location);

}
