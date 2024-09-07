package pl.supereasy.sectors.api.netty.thread;

import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.netty.enums.SourceType;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.conn.SectorRegisterPacket;
import pl.supereasy.sectors.api.packets.impl.status.ServerStatusPacket;
import pl.supereasy.sectors.api.sectors.data.Sector;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class SectorStatusThread {

    private final ScheduledExecutorService executorService;

    public SectorStatusThread(final SectorPlugin plugin) {
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.executorService.scheduleAtFixedRate(() -> {
            try {
                final Sector sector = plugin.getSectorManager().getCurrentSector();
                double tps = MinecraftServer.getServer().recentTps[0];
                final Packet packet = new ServerStatusPacket(sector.getSectorName(), (tps >= 20 ? 20 : sector.getTps()), System.currentTimeMillis());
                plugin.getSectorClient().sendPacket(packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 3, 1, TimeUnit.SECONDS);
    }
}
