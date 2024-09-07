package pl.supereasy.sectors.util.api;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface Itemable extends Nameable{

    Material getMaterial();

    List<String> getLore();

    ItemStack asItemStack();

}
