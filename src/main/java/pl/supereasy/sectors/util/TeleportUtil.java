package pl.supereasy.sectors.util;

import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.synchro.UserChangeSectorPacket;
import pl.supereasy.sectors.api.packets.impl.user.UserSpawnTeleport;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.api.sectors.type.SectorType;
import pl.supereasy.sectors.core.user.impl.User;

import java.util.Comparator;
import java.util.Optional;

public class TeleportUtil {

    public static boolean randomTeleport(Player player, int minX, int maxX, int minZ, int maxZ) {
        int randomX = RandomUtil.getRandInt(minX, maxX);
        int randomZ = RandomUtil.getRandInt(minZ, maxZ);
        int y = player.getWorld().getHighestBlockYAt(randomX, randomZ);
        Location randomLocation = new Location(player.getWorld(), randomX, y, randomZ);
        Biome biome = randomLocation.getBlock().getBiome();
        if (biome == Biome.OCEAN || biome == Biome.DEEP_OCEAN) {
            return randomTeleport(player, minX, maxX, minZ, maxZ);
        }
        player.teleport(randomLocation);
        String teleportMessage = "&8» &fZostales przeteleportowany na koordynaty&8: &cX&8: &f{X} &cY&8: &f{Y} &cZ&8: &f{Z}";
        teleportMessage = teleportMessage.replace("{X}", Integer.toString(player.getLocation().getBlockX()));
        teleportMessage = teleportMessage.replace("{Y}", Integer.toString(player.getLocation().getBlockY()));
        teleportMessage = teleportMessage.replace("{Z}", Integer.toString(player.getLocation().getBlockZ()));
        ChatUtil.sendMessage(player, teleportMessage);
        return true;
    }

    public static void sendToRandomSpawn(final User user, final Player p) {
        final Sector spawn = TeleportUtil.getRandomSpawn();
        final String spawnName = (spawn == null ? "spawn-1" : spawn.getSectorName());
        Packet packet = new UserSpawnTeleport(p.getName(), spawnName);
        SectorPlugin.getInstance().getSectorClient().sendGlobalPacket(packet);
        p.setHealth(20.0);
        p.setFoodLevel(20);
        p.getInventory().clear();
        user.saveUserToValues();
        packet = new UserChangeSectorPacket(user);
        SectorPlugin.getInstance().getSectorClient().sendUser(packet, spawnName);
    }

    public static Location randomLocation(int minX, int maxX, int minZ, int maxZ) {
        final int randomX = RandomUtil.getRandInt((minX + 100), (maxX - 100));
        final int randomZ = RandomUtil.getRandInt((minZ + 100), (maxZ - 100));
        final World world = Bukkit.getWorld("world");
        final int y = world.getHighestBlockYAt(randomX, randomZ);
        Location randomLocation = new Location(world, randomX, y, randomZ);
        Biome biome = randomLocation.getBlock().getBiome();
        if (biome == Biome.OCEAN || biome == Biome.DEEP_OCEAN) {
            return randomLocation(minX, maxX, minZ, maxZ);
        }
        return randomLocation;
    }

    public static Sector getRandomSpawn() {
        final long time = System.currentTimeMillis();
        final Optional<Sector> optSector = SectorPlugin.getInstance().getSectorManager().getSectors().values()
                .stream()
                .filter(sec -> sec.getSectorType() == SectorType.SPAWN)
                .filter(sec -> time - sec.getLastUpdate() <= 7000)
                .min(Comparator.comparing(sec -> sec.getOnlinePlayers().size()));
        return optSector.orElse(null);
    }

}
