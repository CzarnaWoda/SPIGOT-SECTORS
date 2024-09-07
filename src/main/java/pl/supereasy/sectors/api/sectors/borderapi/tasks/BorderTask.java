package pl.supereasy.sectors.api.sectors.borderapi.tasks;

import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.supereasy.bpaddons.bossbar.BarColor;
import pl.supereasy.bpaddons.bossbar.BarStyle;
import pl.supereasy.bpaddons.bossbar.BlazingBossBar;
import pl.supereasy.bpaddons.bossbar.BossBarBuilder;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.core.textcommands.data.TextCommand;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.MathUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BorderTask extends BukkitRunnable {

    private final SectorPlugin sectorPlugin;
    public static final UUID uuid = UUID.randomUUID();

    public BorderTask(SectorPlugin sectorPlugin) {
        this.sectorPlugin = sectorPlugin;
    }

    @Override
    public void run() {
        for (Player online : Bukkit.getOnlinePlayers()) {
                final Sector sector = sectorPlugin.getSectorManager().getCurrentSector();
                //south - max z
                //north - min z
                //east - max x
                //west min x
                //prostokat z wieksza sciana X
                if (Math.abs(sector.getMaxX() - sector.getMinX()) > Math.abs(sector.getMaxZ() - sector.getMinZ())) {
                    final double locationZ = (sector.getDistanceNorth(online.getLocation()) > sector.getDistanceSouth(online.getLocation())) ? sector.getMaxZ() - (sector.getWallSize() / 2.0) : sector.getMinZ() + (sector.getWallSize() / 2.0);
                    final double locationX = (sector.getDistanceEast(online.getLocation()) > sector.getDistanceWest(online.getLocation())) ? sector.getMaxX() - (sector.getWallSize() / 2.0) : sector.getMinX() + (sector.getWallSize() / 2.0);

                    sectorPlugin.getWorldBorderApi().setBorder(online, sector.getWallSize() + sector.getOffset(), new Location(sector.getWorld(), locationX, 0, locationZ));
                }
                //prostokat z wieksza sciana Z
                if (Math.abs(sector.getMaxZ() - sector.getMinZ()) > Math.abs(sector.getMaxX() - sector.getMinX())) {
                    final double locationZ = (sector.getDistanceNorth(online.getLocation()) > sector.getDistanceSouth(online.getLocation())) ? sector.getMaxZ() - (sector.getWallSize() / 2.0) : sector.getMinZ() + (sector.getWallSize() / 2.0);
                    final double locationX = (sector.getDistanceWest(online.getLocation()) > sector.getDistanceEast(online.getLocation())) ? sector.getMaxX() - (sector.getWallSize() / 2.0) : sector.getMinX() + (sector.getWallSize() / 2.0);

                    sectorPlugin.getWorldBorderApi().setBorder(online, sector.getWallSize() + sector.getOffset(), new Location(sector.getWorld(), locationX, 0, locationZ));
                }
                //kwadrat
                if (Math.abs(sector.getMaxZ()) + Math.abs(sector.getMinZ()) == Math.abs(sector.getMaxX()) + Math.abs(sector.getMinX())) {
                    final double locationZ = sector.getMaxZ() - (sector.getWallSize() / 2.0);
                    final double locationX = sector.getMaxX() - (sector.getWallSize() / 2.0);

                    sectorPlugin.getWorldBorderApi().setBorder(online, sector.getWallSize() + sector.getOffset(), new Location(sector.getWorld(), locationX, 0, locationZ));
            }
        }
    }
}
