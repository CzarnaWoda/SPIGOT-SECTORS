package pl.supereasy.sectors.util.api;

import org.bukkit.inventory.ItemStack;

public abstract class Item {

    private final ItemStack itemStack;

    public Item(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
