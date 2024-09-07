package pl.supereasy.sectors.core.autofarmer.api;

import org.bukkit.inventory.ItemStack;

public interface AutomaticFarmerManager {

    AutomaticFarmer getFarmer(final ItemStack itemStack);

    default void registerFarm(final ItemStack itemStack, final AutomaticFarmer automaticFarmer) {
        if (itemStack.getItemMeta() == null || itemStack.getItemMeta().getDisplayName() == null) {
            throw new RuntimeException("Cannot register item without name!");
        }
        registerFarm(itemStack.getItemMeta().getDisplayName(), automaticFarmer);
    }


    void registerFarm(final String name, final AutomaticFarmer automaticFarmer);


}
