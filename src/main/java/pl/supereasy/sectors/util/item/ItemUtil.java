package pl.supereasy.sectors.util.item;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public final class ItemUtil {

    public static int getAmountOfItem(final Material material, final Player player, final short durability) {
        int amount = 0;
        ItemStack[] contents;
        int length = (contents = player.getInventory().getContents()).length;
        for (int i = 0; i < length; i++) {
            ItemStack itemStack = contents[i];
            if ((itemStack != null) && (itemStack.getType().equals(material)) && (itemStack.getDurability() == durability)) {
                amount += itemStack.getAmount();
            }
        }
        return amount;
    }

    public static int removeItems(Inventory inventory, Material type, int amount) {
        if ((type == null) || (inventory == null)) {
            return -1;
        }
        if (amount <= 0) {
            return -1;
        }
        if (amount == Integer.MAX_VALUE) {
            inventory.remove(type);
            return 0;
        }
        HashMap<Integer, ItemStack> retVal = inventory.removeItem(new ItemStack(type, amount));

        int notRemoved = 0;
        for (ItemStack item : retVal.values()) {
            notRemoved += item.getAmount();
        }
        return notRemoved;
    }
}