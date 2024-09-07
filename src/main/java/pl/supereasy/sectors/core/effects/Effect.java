package pl.supereasy.sectors.core.effects;


import lombok.Data;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import pl.supereasy.sectors.util.ItemBuilder;

import java.util.List;

@Data
public class Effect{


    @NonNull private String guiName;
    @NonNull private List<String> guiLore;
    @NonNull private Material guiItem;
    @NonNull private short guiItemData;
    @NonNull private List<PotionEffect> potionEffects;
    @NonNull private List<ItemStack> effectCost;
    @NonNull private int index;
    private ItemBuilder itemBuilder;

    public void build() {
        itemBuilder = new ItemBuilder(guiItem,1,guiItemData).setTitle(guiName).addLores(guiLore);
    }

}
