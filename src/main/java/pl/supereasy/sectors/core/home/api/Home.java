package pl.supereasy.sectors.core.home.api;

import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.api.teleport.api.Teleportable;

import java.io.Serializable;

public interface Home extends Teleportable, Serializable {

    Sector getSector();

}
