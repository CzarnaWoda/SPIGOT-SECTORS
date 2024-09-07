package pl.supereasy.sectors.core.warp.api;

import pl.supereasy.sectors.api.redis.api.RedisData;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.api.teleport.api.Teleportable;

public interface Warp extends Teleportable, RedisData {

    Sector getSector();

}
