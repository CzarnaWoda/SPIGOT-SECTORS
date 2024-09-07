package pl.supereasy.sectors.guilds.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.log.api.GuildLogAction;
import pl.supereasy.sectors.guilds.log.impl.GuildLogActionImpl;
import pl.supereasy.sectors.util.ItemBuilder;
import pl.supereasy.sectors.util.TimeUtil;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GuildLogActionInventory {

    public static InventoryGUI getGuildLogInventory(final Guild guild) {
        final InventoryGUI gui = new InventoryGUI("&6&lLogi gildii &8[&2" + guild.getTag() + "&8]", 5);
        final AtomicInteger ai = new AtomicInteger(0);
        final List<GuildLogActionImpl> actions = guild.getLogActions();
        for (int i = 0; i < actions.size(); i++) {
            final GuildLogAction logAction = actions.get(i);
            final ItemBuilder is = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3)
                    .setTitle("&8->> &8( &6&l" + logAction.getPlayerName() + " &8) <<-").addLore("");
            is.addLores(logAction.getLogDescription());
            gui.setItem(ai.getAndIncrement(), is.build(), null);
        }
        return gui;
    }

}
