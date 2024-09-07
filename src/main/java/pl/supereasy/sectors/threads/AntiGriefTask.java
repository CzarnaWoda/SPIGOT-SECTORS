package pl.supereasy.sectors.threads;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.threads.api.Schedulable;
import pl.supereasy.sectors.threads.api.ThreadExecutorAPI;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AntiGriefTask extends Schedulable {



    @Override
    public void start() {
        ThreadExecutorAPI.SCHEDULED.scheduleAtFixedRate(() -> {
            SectorPlugin.getInstance().getAntiGriefManager().getAntiGriefBlockLocations().forEach((location, time) -> {
                if (time <= System.currentTimeMillis()) {
                    SectorPlugin.getInstance().getAntiGriefManager().removeBlockLocation(location.getBlock());
                }
            });
        }, 20, 20, TimeUnit.SECONDS);
    }
}
