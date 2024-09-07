package pl.supereasy.sectors.core.antigrief;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.redisson.api.RMap;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.redis.channels.RedisChannel;
import pl.supereasy.sectors.util.LocationUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class AntiGriefManager {

    private SectorPlugin plugin;
    private final Map<Location, Long> antiGriefBlockLocations;

    public AntiGriefManager(SectorPlugin plugin) {
        this.plugin = plugin;
        this.antiGriefBlockLocations = new ConcurrentHashMap<>();
        RedisChannel.INSTANCE.ANTIGRIEF.forEach((loc, time) -> {
            if (time <= System.currentTimeMillis()) {
                final Location blockLocation = LocationUtil.convertStringToBlockLocation(loc);
                if (blockLocation.getBlock() != null && blockLocation.getBlock().getType() != Material.AIR) {
                    blockLocation.getBlock().setType(Material.AIR);
                    RedisChannel.INSTANCE.ANTIGRIEF.remove(loc);
                }
            } else {
                antiGriefBlockLocations.put(LocationUtil.convertStringToBlockLocation(loc), RedisChannel.INSTANCE.ANTIGRIEF.get(loc));
            }
        });
        System.out.println("ANTIGRIEF -> Loaded " + antiGriefBlockLocations.size() + " locations");
    }

    public void addBlockLocation(Block block){
        RedisChannel.INSTANCE.ANTIGRIEF.putAsync(LocationUtil.convertLocationBlockToString(block.getLocation()), System.currentTimeMillis() + 5 * 60 * 1000L);
        antiGriefBlockLocations.put(block.getLocation(), System.currentTimeMillis() + 5 * 60 * 1000L);
    }
    public void removeBlockLocation(Block block){
        if(block != null && block.getType() != Material.AIR) {
            block.setType(Material.AIR);
            RedisChannel.INSTANCE.ANTIGRIEF.remove(LocationUtil.convertLocationBlockToString(block.getLocation()));
            antiGriefBlockLocations.remove(block.getLocation());
        }
    }


}
