package pl.supereasy.sectors.core.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ActionBarUtil;
import pl.supereasy.sectors.util.ChatUtil;

@RequiredArgsConstructor
public class VanishListener implements Listener {

    private final SectorPlugin plugin;

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        final Player player = e.getPlayer();
        final User user = plugin.getUserManager().getUser(player.getUniqueId());
        if(user.getGroup().hasPermission(UserGroup.HELPER)){
            plugin.getVanishManager().applyVanishToPlayer(player);
            ActionBarUtil.sendActionBar(player, ChatUtil.fixColor("&8->> &7Automatycznie nadano ci &aVANISH'a &8<<-"));
        }else {
            for (Player vanished : plugin.getVanishManager().getVanished()) {
                if (vanished != null && vanished.isOnline()) {
                    player.hidePlayer(vanished);
                }
            }
        }
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        plugin.getVanishManager().getVanished().remove(e.getPlayer());
    }
    @EventHandler
    public void onPickUp(PlayerPickupItemEvent e){
        if (plugin.getVanishManager().getVanished().contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        if (plugin.getVanishManager().getVanished().contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }
}
