package pl.supereasy.sectors.core.craftings.api;

import org.bukkit.Material;

public interface CraftingSlot {

    int getSlot();

    Material getMaterial();

    int getAmount();

}
