package pl.supereasy.sectors.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import pl.supereasy.sectors.core.user.impl.UserJoinManager;
import pl.supereasy.sectors.util.DamageUtil;

public class AfterJoinListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player) {
            if (UserJoinManager.hasJoinedNewly(event.getEntity().getUniqueId())) {
                event.setCancelled(true);
            }
            final Player damager = DamageUtil.getDamager(event);
            if(damager != null){
                if (UserJoinManager.hasJoinedNewly(damager.getUniqueId())) {
                    event.setCancelled(true);
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event){
        if(event.getEntity() instanceof Player) {
            if (UserJoinManager.hasJoinedNewly(event.getEntity().getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent e){
        final Player player = (Player)e.getWhoClicked();
        if (UserJoinManager.hasJoinedNewly(player.getUniqueId())) {
            e.setCancelled(true);
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrag(InventoryDragEvent e){
        final Player player = (Player)e.getWhoClicked();
        if (UserJoinManager.hasJoinedNewly(player.getUniqueId())) {
            e.setCancelled(true);
        }
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void interact(PlayerInteractEvent e){
        if (UserJoinManager.hasJoinedNewly(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void interacte(PlayerInteractAtEntityEvent e){
        if (UserJoinManager.hasJoinedNewly(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPickup(PlayerPickupItemEvent e){
        if (UserJoinManager.hasJoinedNewly(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onDrop(PlayerDropItemEvent e){
        if (UserJoinManager.hasJoinedNewly(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void amror(PlayerArmorStandManipulateEvent e){
        if (UserJoinManager.hasJoinedNewly(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }
}
