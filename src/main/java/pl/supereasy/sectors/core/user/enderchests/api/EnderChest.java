package pl.supereasy.sectors.core.user.enderchests.api;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.util.ItemStackUtil;

public interface EnderChest {

    ItemStack[] getContent();

    Inventory getInventory();

    default String asSerializable() {
        return ItemStackUtil.itemStackArrayToBase64(getContent());
    }


}
