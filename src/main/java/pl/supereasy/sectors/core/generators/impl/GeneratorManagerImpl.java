package pl.supereasy.sectors.core.generators.impl;

import org.bukkit.Location;
import org.bukkit.Material;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.redis.channels.RedisChannel;
import pl.supereasy.sectors.core.generators.api.BlockGenerator;
import pl.supereasy.sectors.core.generators.api.GeneratorManager;
import pl.supereasy.sectors.util.GsonUtil;
import pl.supereasy.sectors.util.LocationUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class GeneratorManagerImpl implements GeneratorManager {

    private final Map<Location, BlockGenerator> generatorMap = new HashMap<>();


    public GeneratorManagerImpl() {
        RedisChannel.INSTANCE.STONIARKI.forEach((loc, generatorObj) -> {
            final Location location = LocationUtil.convertStringToBlockLocation(loc);
            final BlockGenerator blockGenerator = GsonUtil.fromJson(generatorObj, BlockGeneratorImpl.class);
            this.generatorMap.putIfAbsent(location, blockGenerator);
        });
        SectorPlugin.getInstance().getLogger().log(Level.INFO, "Zaladowano " + this.generatorMap.size() + " generatorow!");
    }

    @Override
    public BlockGenerator getGenerator(Location location) {
        return this.generatorMap.get(location);
    }

    @Override
    public boolean isGenerator(Location location) {
        return this.generatorMap.get(location) != null;
    }

    @Override
    public void addGenerator(Location location, BlockGenerator blockGenerator) {
        this.generatorMap.putIfAbsent(location, blockGenerator);
        RedisChannel.INSTANCE.STONIARKI.putAsync(LocationUtil.convertLocationBlockToString(location), GsonUtil.toJson(blockGenerator));
    }

    @Override
    public void removeGenerator(Location location) {
        this.generatorMap.remove(location);
        RedisChannel.INSTANCE.STONIARKI.removeAsync(LocationUtil.convertLocationBlockToString(location));
    }
}
