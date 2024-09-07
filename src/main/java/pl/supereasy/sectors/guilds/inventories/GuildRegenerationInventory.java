package pl.supereasy.sectors.guilds.inventories;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.util.ItemBuilder;

public class GuildRegenerationInventory {

    public static InventoryGUI XD(final Guild guild) {
        InventoryGUI inv = new InventoryGUI("&6&lRegeneracja &8[&2" + guild.getTag() + "&8]", 3);

        final ItemStack cyanGlass = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 9).setTitle("&7#").build();
        final ItemStack purpleGlass = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 10).setTitle("&7#").build();
        final ItemStack blueGlass = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 11).setTitle("&7#").build();

        int[] cyan_ints = {0, 1, 7, 8, 9, 10, 11, 15, 16, 17, 19, 20, 24, 25, 28, 29, 33, 34, 36, 37, 38, 42, 43, 44, 45, 46, 52, 53};
        int[] purple_ints = {2, 3, 5, 6, 12, 13, 14, 18, 26, 27, 35, 39, 40, 41, 47, 48, 50, 51};
        int[] blue_ints = {4, 49};
        //21,22,23,30,31,32 itemy
        for (int purple_int : purple_ints) {
            inv.setItem(purple_int, purpleGlass, null);
        }
        for (int cyan_int : cyan_ints) {
            inv.setItem(cyan_int, cyanGlass, null);
        }
        for (int blue_int : blue_ints) {
            inv.setItem(blue_int, blueGlass, null);
        }

        final ItemStack chests = new ItemBuilder(Material.CHEST).setTitle("&8->> ( &cSkrzynki &8) <<-")
                .addLore("  &8->> &7Ilosc: &a&n").build();

        return inv;
    }

}
