package pl.supereasy.sectors.api.inventory.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.IAction;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryListener implements Listener {


    private static final Map<Inventory, InventoryGUI> inventories = new HashMap<>();
    private final HashMap<UUID, Long> times = new HashMap<>();

    @EventHandler
    public void onClick(final InventoryClickEvent e){
        final Player player = (Player) e.getWhoClicked();
        if (SectorPlugin.getInstance().getTeleportManager().hasTeleportRequest(player.getUniqueId())) {
            ChatUtil.sendMessage(player, " &8» &cNie mozesz klikac w inventory podczas teleportacji!");
            e.setCancelled(true);
            return;
        }
        InventoryGUI gui = inventories.get(e.getInventory());
        if (gui == null) return;
        e.setCancelled(true);
        if (SectorPlugin.getInstance().getTeleportManager().hasTeleportRequest(player.getUniqueId())) {
            ChatUtil.sendMessage(player, " &8» &cNie mozesz klikac w inventory podczas teleportacji!");
            e.setCancelled(true);
            return;
        }
        IAction action = gui.getActions().get(e.getRawSlot());
        if (action != null) {
            final Long time = this.times.get(player.getUniqueId());
            final User u = SectorPlugin.getInstance().getUserManager().getUser(player.getUniqueId());
            if (!u.getGroup().hasPermission(UserGroup.ADMIN) && time != null && time > System.currentTimeMillis()) {
                ChatUtil.sendMessage(player, "&4Blad: &cNie mozesz tak czesto klikac !");
            } else {
                times.put(player.getUniqueId(), System.currentTimeMillis() + 500L);
                action.execute(player, e.getInventory(), e.getRawSlot(), e.getInventory().getItem(e.getRawSlot()));
            }
        }
    }

    @EventHandler
    public void onClose(final InventoryCloseEvent event) {
        InventoryGUI gui = inventories.get(event.getInventory());
        if (gui == null) {
            return;
        }
        inventories.remove(event.getInventory());
    }

    public static Map<Inventory, InventoryGUI> getInventories() {
        return inventories;
    }
}
