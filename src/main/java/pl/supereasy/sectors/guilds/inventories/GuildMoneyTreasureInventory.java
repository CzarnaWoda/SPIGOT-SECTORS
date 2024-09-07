package pl.supereasy.sectors.guilds.inventories;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.inventories.handlers.TreasureCoinsHandler;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemBuilder;

public class GuildMoneyTreasureInventory {

    private static final ItemStack coins10 = new ItemBuilder(Material.DOUBLE_PLANT).setTitle("&8->> ( &d&lWplata &8) <<-")
            .addLore("&8  * &7Kwota: &610").build();
    private static final ItemStack coins50 = new ItemBuilder(Material.DOUBLE_PLANT).setTitle("&8->> ( &d&lWplata &8) <<-")
            .addLore("&8  * &7Kwota: &650").build();
    private static final ItemStack coins100 = new ItemBuilder(Material.DOUBLE_PLANT).setTitle("&8->> ( &d&lWplata &8) <<-")
            .addLore("&8  * &7Kwota: &6100").build();


    //TODO w razie czego automatyzacja SET<Integer>//kwota i generowac

    public static InventoryGUI getTreasureCoinsInventory(final User user, final Guild guild) {
        InventoryGUI inv = new InventoryGUI("&6&lSkarbiec gildii &8[&2" + guild.getTag() + "&8]", 3);

        int[] blue = {2, 3, 4, 5, 6, 10, 16, 18, 20, 22, 24};
        int[] purple = {0, 1, 7, 8, 9, 17, 19, 21, 23, 25};

        final ItemBuilder pg = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 10).setTitle(ChatUtil.fixColor("&d#"));
        final ItemBuilder bg = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 11).setTitle(ChatUtil.fixColor("&3#"));
        final ItemBuilder money = new ItemBuilder(Material.GOLD_NUGGET,1).setTitle(ChatUtil.fixColor("&8->> ( &6STAN SKARBCA &8 ) <<-"))
                .addLore(ChatUtil.fixColor("&8* &7Monety: " + guild.getGuildTreasury().getCoins()));
        for (int i : blue) {
            inv.setItem(i, bg.build(), null);
        }
        for (int i : purple) {
            inv.setItem(i, pg.build(), null);
        }

        inv.setItem(11, coins10, new TreasureCoinsHandler(user, guild, 10));
        inv.setItem(13, coins50, new TreasureCoinsHandler(user, guild, 50));
        inv.setItem(15, coins100, new TreasureCoinsHandler(user, guild, 100));
        inv.setItem(26,money.build(),null);
        return inv;
    }
}
