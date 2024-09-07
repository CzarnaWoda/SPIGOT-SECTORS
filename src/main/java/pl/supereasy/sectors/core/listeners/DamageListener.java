package pl.supereasy.sectors.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import pl.supereasy.sectors.SectorPlugin;

public class DamageListener implements Listener {

    private final SectorPlugin plugin;

    public DamageListener(SectorPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(final EntityDamageEvent e){
        if(e.isCancelled()) return;
        if(e.getEntity() instanceof Player && e.getDamage() > 0){
            final Player player = (Player) e.getEntity();
            this.plugin.getTeleportManager().deleteRequestIfExists(player); //Teleport
        }
    }

}
