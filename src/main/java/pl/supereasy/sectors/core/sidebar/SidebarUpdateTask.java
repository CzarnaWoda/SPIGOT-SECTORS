package pl.supereasy.sectors.core.sidebar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.sectors.type.SectorType;

public class SidebarUpdateTask extends BukkitRunnable {
    private  final  SectorPlugin plugin;

    public SidebarUpdateTask(SectorPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for(Player player : Bukkit.getOnlinePlayers()){
            plugin.getEasyScoreboardManager().updateScoreBoard(player);
        }
    }
}
