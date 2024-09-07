package pl.supereasy.sectors.guilds.inventories;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.util.ItemBuilder;

public class GuildMainTreasureInventory {

    private final static ItemStack itemTreasure = new ItemBuilder(Material.STAINED_CLAY, 1, (short) 5).setTitle("&8->> ( &6&nSKARBIEC Z ITEMAMI&8 ) <<-").build();
    private final static ItemStack coinsTreasure = new ItemBuilder(Material.STAINED_CLAY, 1, (short) 10).setTitle("&8->> ( &6&nSKARBIEC Z MONETAMI&8 ) <<-").build();


    public static InventoryGUI getTreasureMainInventory(final User user, final Guild guild) {
        InventoryGUI inv = new InventoryGUI("&6&lSkarbiec gildii &8[&2" + guild.getTag() + "&8]", 1);
        inv.setItem(3, itemTreasure, ((paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            paramPlayer.openInventory(guild.getGuildTreasury().getInventory());
        }));
        inv.setItem(5, coinsTreasure, ((paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            GuildMoneyTreasureInventory.getTreasureCoinsInventory(user, guild).openInventory(paramPlayer);
        }));
        return inv;
    }

}
