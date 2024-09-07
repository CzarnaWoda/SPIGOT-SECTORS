package pl.supereasy.sectors.core.autofarmer.impl;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.autofarmer.api.AutomaticFarmer;
import pl.supereasy.sectors.guilds.Guild;

public class KopaczFosy implements AutomaticFarmer {

    private final Material material = Material.AIR;

    @Override
    public void run(Location location) {
        Guild guild = SectorPlugin.getInstance().getGuildManager().getGuild(location);
        location.getBlock().setType(material);
        new BukkitRunnable() {
            @Override
            public void run() {
                Block b = location.subtract(0, 1, 0).getBlock();
                if (guild != null && guild.getGuildRegion().isCenter(b.getLocation())) {
                    cancel();
                    return;
                }
                if (b.getType() == Material.BEDROCK) {
                    cancel();
                    return;
                }
                b.setType(material);
            }
        }.runTaskTimer(SectorPlugin.getInstance(), 20, 0);
    }

}
