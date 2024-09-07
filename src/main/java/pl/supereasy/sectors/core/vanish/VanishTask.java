package pl.supereasy.sectors.core.vanish;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.util.ActionBarUtil;
import pl.supereasy.sectors.util.ChatUtil;

@RequiredArgsConstructor
public class VanishTask extends BukkitRunnable {

    private final SectorPlugin plugin;
    @Override
    public void run() {
        for(Player player : plugin.getVanishManager().getVanished()){
            if(player != null && player.isOnline()){
                ActionBarUtil.sendActionBar(player, ChatUtil.fixColor("&8->> &7AKTUALNIE JESTES &bNIEWIDZIALNY &8<<-"));
            }
        }
    }
}
