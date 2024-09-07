package pl.supereasy.sectors.guilds.api;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface GuildTreasury {

    Inventory getInventory();

    ItemStack[] getItems();

    int getCoins();

    void addCoins(final int value);

    void removeCoins(final int value);

    //logs
}
