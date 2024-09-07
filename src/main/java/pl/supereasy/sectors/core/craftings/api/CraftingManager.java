package pl.supereasy.sectors.core.craftings.api;

import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.core.craftings.enums.CraftItem;

import java.util.List;
import java.util.Map;

public interface CraftingManager {

    void registerRecipe(final CraftingRecipe craftingRecipe);

    void registerRecipe(final String name, final ItemStack result, final List<CraftingSlot> craftingSlotList, String autoCrafting);

    default void unregisterRecipe(final CraftItem craftItem) {
        unregisterRecipe(craftItem.name());
    }

    void unregisterRecipe(final String name);

    Map<String, CraftingRecipe> getCraftingRecipes();

    CraftingRecipe findRecipe(final ItemStack itemStack);

    CraftingRecipe getRecipe(final String recipeName);

    CraftingRecipe getRecipe(final CraftItem craftItem);

    default CraftingRecipe findRecipe(final CraftItem craftItem) {
        return getCraftingRecipes().get(craftItem.name());
    }

    List<CraftingRecipe> getCraftingRecipeList();

}
