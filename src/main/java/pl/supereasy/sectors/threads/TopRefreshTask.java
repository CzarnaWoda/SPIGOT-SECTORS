package pl.supereasy.sectors.threads;

import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.threads.api.Schedulable;
import pl.supereasy.sectors.threads.api.ThreadExecutorAPI;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TopRefreshTask extends Schedulable {

    @Override
    public void start() {
        ThreadExecutorAPI.SCHEDULED.scheduleAtFixedRate(() -> {
            try {
                SectorPlugin.getInstance().getTopManager().refreshTops();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 10, 120, TimeUnit.SECONDS);
    }
}
