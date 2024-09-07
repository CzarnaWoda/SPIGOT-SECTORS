package pl.supereasy.sectors.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;

public class FoodChangeLevelListener implements Listener {
    private SectorPlugin plugin;

    public FoodChangeLevelListener(SectorPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onFoodChangeLevel(FoodLevelChangeEvent e){
        if(e.isCancelled()){
            return;
        }
        if(e.getEntity() instanceof Player){
            final Player player = (Player) e.getEntity();
            final User user = plugin.getUserManager().getUser(player.getUniqueId());
            if(user.getGroup().hasPermission(UserGroup.ENIU)){
                if(e.getFoodLevel() != 20){
                    e.setFoodLevel(20);
                }
                e.setCancelled(true);
            }
        }
    }

}
