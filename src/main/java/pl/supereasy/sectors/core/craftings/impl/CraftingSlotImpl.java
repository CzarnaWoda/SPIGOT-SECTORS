package pl.supereasy.sectors.core.craftings.impl;

import org.bukkit.Material;
import pl.supereasy.sectors.core.craftings.api.CraftingSlot;

public class CraftingSlotImpl implements CraftingSlot {

    private final int slot;
    private final Material material;
    private final int amount;

    public CraftingSlotImpl(int slot, Material material, int amount) {
        this.slot = slot;
        this.material = material;
        this.amount = amount;
    }

    @Override
    public int getSlot() {
        return this.slot;
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }

    @Override
    public int getAmount() {
        return this.amount;
    }
}
