package pl.supereasy.sectors.core.craftings.api;

import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CraftingRecipe {

    String getName();

    List<CraftingSlot> getSlots();

    ItemStack getResult();

    String getAutoCrafting();


}
