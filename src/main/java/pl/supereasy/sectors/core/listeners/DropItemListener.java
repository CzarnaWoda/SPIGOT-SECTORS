package pl.supereasy.sectors.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.teleport.impl.TeleportManagerImpl;
import pl.supereasy.sectors.util.ChatUtil;

public class DropItemListener implements Listener {

    private final SectorPlugin plugin;

    public DropItemListener(SectorPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDrop(final PlayerDropItemEvent e) {
        if (e.isCancelled()) return;
        if (this.plugin.getTeleportManager().hasTeleportRequest(e.getPlayer().getUniqueId())) {
            ChatUtil.sendMessage(e.getPlayer(), " &8» &cNie mozesz wyrzucac itemow podczas teleportacji!");
            e.setCancelled(true);
            return;
        }
        if(TeleportManagerImpl.cancelActionCache.getIfPresent(e.getPlayer().getUniqueId()) != null){
            ChatUtil.sendMessage(e.getPlayer(), " &8» &cNie mozesz wyrzucac itemow podczas teleportacji!");
            e.setCancelled(true);
            return;
        }
        if (this.plugin.getSectorManager().getCurrentSector().howCloseBorderInSector(e.getPlayer().getLocation()) <= 30.0) {
            e.setCancelled(true);
            ChatUtil.sendMessage(e.getPlayer(), " &8» &cNie mozesz wyrzucac itemow na granicy z sektorem!");
            return;
        }
    }

}
