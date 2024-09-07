package pl.supereasy.sectors.core.craftings.impl;

import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.core.craftings.api.CraftingRecipe;
import pl.supereasy.sectors.core.craftings.api.CraftingSlot;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class CraftingRecipeImpl implements CraftingRecipe {

    private final String recipeName;
    private final ItemStack itemResult;
    private final List<CraftingSlot> craftingSlots;
    private final String autoCrafting;

    public CraftingRecipeImpl(String recipeName, ItemStack itemResult, List<CraftingSlot> craftingSlotList,String autoCrafting) {
        this.recipeName = recipeName;
        this.itemResult = itemResult;
        this.craftingSlots = craftingSlotList;
        this.autoCrafting = autoCrafting;
    }

    @Override
    public String getName() {
        return this.recipeName;
    }

    @Override
    public List<CraftingSlot> getSlots() {
        return this.craftingSlots;
    }

    @Override
    public ItemStack getResult() {
        return this.itemResult;
    }

    @Override
    public String getAutoCrafting() {
        return autoCrafting;
    }
}
