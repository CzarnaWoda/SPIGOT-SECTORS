package pl.supereasy.sectors.core.combat.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.combat.data.Combat;
import pl.supereasy.sectors.util.ActionBarUtil;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.UUID;

public class CombatActionBarUpdate extends BukkitRunnable {

    private final SectorPlugin plugin;

    public CombatActionBarUpdate(SectorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for(Player p : Bukkit.getOnlinePlayers()){
            final Combat c = plugin.getCombatManager().getCombat(p);
            if(c != null && c.hasFight()){
                long secondsLeft = ((c.getLastAttackTime() / 1000))
                        - (System.currentTimeMillis() / 1000);
                if(secondsLeft >= 0){
                    ActionBarUtil.sendActionBar(p, ChatUtil.fixColor("&8->> ( &cANTYLOGOUT &4&n" + secondsLeft + "&8 ) <<-"));
                }
            }
        }

    }
}
