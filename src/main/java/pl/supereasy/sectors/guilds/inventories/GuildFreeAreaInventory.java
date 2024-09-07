package pl.supereasy.sectors.guilds.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.redisson.api.RSet;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.guilds.area.GuildArea;
import pl.supereasy.sectors.util.ItemBuilder;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class GuildFreeAreaInventory {

    private static final ItemBuilder lime = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 5).setTitle("");
    private static final ItemBuilder yellow = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 4).setTitle("");

    public static InventoryGUI getFreeAreaGUI() {
        InventoryGUI gui = new InventoryGUI("&cWolny teren:", 6);
        final AtomicInteger ai = new AtomicInteger(0);
        gui.setItem(0, yellow.build(), null);
        gui.setItem(1, lime.build(), null);
        gui.setItem(2, yellow.build(), null);
        gui.setItem(3, lime.build(), null);
        gui.setItem(4, yellow.build(), null);
        gui.setItem(5, lime.build(), null);
        gui.setItem(6, yellow.build(), null);
        gui.setItem(7, lime.build(), null);
        gui.setItem(8, yellow.build(), null);
        gui.setItem(9, lime.build(), null);
        gui.setItem(17, lime.build(), null);
        gui.setItem(18, yellow.build(), null);
        gui.setItem(26, yellow.build(), null);
        gui.setItem(27, lime.build(), null);
        gui.setItem(35, lime.build(), null);
        gui.setItem(36, yellow.build(), null);
        gui.setItem(44, yellow.build(), null);
        gui.setItem(45, lime.build(), null);
        gui.setItem(46, yellow.build(), null);
        gui.setItem(47, lime.build(), null);
        gui.setItem(48, yellow.build(), null);
        gui.setItem(49, lime.build(), null);
        gui.setItem(50, yellow.build(), null);
        gui.setItem(51, lime.build(), null);
        gui.setItem(52, yellow.build(), null);
        gui.setItem(53, lime.build(), null);
        for (GuildArea freeArea : SectorPlugin.getInstance().getSectorManager().getCurrentSector().getGuildAreas()) {
            if (ai.getAndIncrement() >= 28) break;
            final ItemBuilder item = new ItemBuilder(Material.WOOL, 1, (short) 13)
                    .setTitle(" &6Wolny teren &8(&e#" + ai.get() + "&8)")
                    .addLore(" ").addLore("&6Koordynaty terenu")
                    .addLore(" &8» &7X: &e" + freeArea.getLocX())
                    .addLore(" &8» &7Z: &e" + freeArea.getLocZ())
                    .addLore(" &8» &7Y: &eDowolne")
                    .addLore(" ").addLore(" &eKliknij, aby zobaczyc teren!");
            gui.addItem(item.build(), null);
        }
        return gui;
    }

}
