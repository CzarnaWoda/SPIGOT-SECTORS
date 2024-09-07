package pl.supereasy.sectors.core.combat.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.combat.data.Combat;
import pl.supereasy.sectors.util.ActionBarUtil;
import pl.supereasy.sectors.util.ChatUtil;

public class CombatTask extends BukkitRunnable {
    private final SectorPlugin plugin;

    public CombatTask(SectorPlugin plugin){
        this.plugin = plugin;
    }
    @Override
    public void run() {
        for(Player player : Bukkit.getOnlinePlayers()){
            final Combat c = plugin.getCombatManager().getCombat(player);
            if(c != null){
                if(c.wasFight() && !c.hasFight()){
                    ChatUtil.sendMessage(player, "&cANTYLOGOUT &8->> &aSkonczyles walke, mozesz sie wylogowac");
                    c.setLastAttackPlayer(null);
                    c.getDamage().clear();
                    c.getAssists().clear();
                    ActionBarUtil.sendActionBar(player, ChatUtil.fixColor("&8->> ( &aKONIEC ANTYLOGOUT &8) <<-"));
                }
            }
        }
    }
}
