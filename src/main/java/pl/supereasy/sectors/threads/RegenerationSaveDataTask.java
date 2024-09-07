package pl.supereasy.sectors.threads;

import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.threads.api.Schedulable;
import pl.supereasy.sectors.guilds.regeneration.impl.RegenerationBlock;
import pl.supereasy.sectors.threads.api.ThreadExecutorAPI;
import pl.supereasy.sectors.util.LocationUtil;

import java.sql.PreparedStatement;
import java.util.Queue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class RegenerationSaveDataTask extends Schedulable {

    @Override
    public void start() {
        ThreadExecutorAPI.SCHEDULED.scheduleAtFixedRate(() -> {//TODO moze redis rdb??
            try {
                final PreparedStatement statement = SectorPlugin.getInstance().getMySQL().generateStatement("INSERT INTO `regeneration_blocks` (id, guildTag, sectorID, blockLocation, blockType) VALUES(?, ?, ?, ?, ?)");
                int val = 5;
                final Queue<RegenerationBlock> blocks = SectorPlugin.getInstance().getGuildRegeneration().getQueue();
                if (blocks.size() < 1) {
                    return;
                }
                if (blocks.size() < 5) {
                    val = blocks.size();
                }
                for (int i = 0; i < val; i++) {
                    final RegenerationBlock regenerationBlock = SectorPlugin.getInstance().getGuildRegeneration().getQueue().remove();
                    statement.setString(1, null);
                    statement.setString(2, regenerationBlock.getGuild().getTag());
                    statement.setInt(3, regenerationBlock.getGuild().getGuildSector().getUniqueSectorID());
                    statement.setString(4, LocationUtil.convertLocationBlockToString(regenerationBlock.getLocation()));
                    statement.setString(5, regenerationBlock.getMaterial().name());
                    statement.executeUpdate();
                    SectorPlugin.getInstance().getLogger().log(Level.INFO, "RegenerationSaveDataTask INSERT blocks");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 5, 10, TimeUnit.SECONDS);
    }


}
