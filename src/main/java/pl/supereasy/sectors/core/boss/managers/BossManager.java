package pl.supereasy.sectors.core.boss.managers;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.redisson.api.RMap;
import pl.supereasy.sectors.core.boss.data.BossReward;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface BossManager {

    void createBoss(final Location location);

    RMap<UUID, String> getBosses();

    List<BossReward> getRewards();

    void addReward(BossReward reward);
    void unload();
    void load();
}
