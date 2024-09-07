package pl.supereasy.sectors.threads;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.user.UserStatsUpdatePacket;
import pl.supereasy.sectors.threads.api.Schedulable;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.threads.api.ThreadExecutorAPI;
import pl.supereasy.sectors.util.Time;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class StatsUpdateTask extends Schedulable {


    @Override
    public void start() {
        ThreadExecutorAPI.SCHEDULED.scheduleAtFixedRate(() -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                final User u = SectorPlugin.getInstance().getUserManager().getUser(p.getUniqueId());
                if (u == null) {
                    return;
                }
                if (u.getLastUpdate() + Time.MINUTE.getTime(3) < System.currentTimeMillis()) {

                    final long time = System.currentTimeMillis() - u.getLastJoin();
                    u.addTimePlay(time);
                    u.setLastJoin(System.currentTimeMillis());

                    final Packet packet = new UserStatsUpdatePacket(u.getUUID(), u.getOpenedCase(), u.getMinedStone(), u.getSpendMoney(), u.getTimePlay(), u.getLastJoin());
                    SectorPlugin.getInstance().getSectorClient().sendGlobalPacket(packet);
                }
            }
            SectorPlugin.getInstance().getLogger().log(Level.INFO, "UserStatsUpdatePacket sent to all sectors!");
        }, 0, 80, TimeUnit.SECONDS);
    }


}
