package pl.supereasy.sectors.core.generators.api;

import org.bukkit.Location;

public interface GeneratorManager {

    BlockGenerator getGenerator(final Location location);

    boolean isGenerator(final Location location);

    void addGenerator(final Location location, final BlockGenerator blockGenerator);

    void removeGenerator(final Location location);


}
