package pl.supereasy.sectors.core.autofarmer.impl;

import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.autofarmer.api.AutomaticFarmer;
import pl.supereasy.sectors.core.autofarmer.api.AutomaticFarmerManager;
import pl.supereasy.sectors.core.craftings.api.CraftingRecipe;
import pl.supereasy.sectors.core.craftings.enums.CraftItem;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class AutomaticFarmerManagerImpl implements AutomaticFarmerManager {

    private final SectorPlugin plugin;
    private final Map<String, AutomaticFarmer> automaticFarmerMap;

    public AutomaticFarmerManagerImpl(SectorPlugin plugin) {
        this.plugin = plugin;
        this.automaticFarmerMap = new HashMap<>();
        final CraftingRecipe boyfarmer = plugin.getCraftingManager().findRecipe(CraftItem.BOYFARMER);
        registerFarm(boyfarmer.getResult().getItemMeta().getDisplayName(), new BoyFarmer());
        final CraftingRecipe kopaczfosy = plugin.getCraftingManager().findRecipe(CraftItem.KOPACZFOSY);
        registerFarm(kopaczfosy.getResult().getItemMeta().getDisplayName(), new KopaczFosy());
        final CraftingRecipe sandfarmer = plugin.getCraftingManager().findRecipe(CraftItem.SANDFARMER);
        registerFarm(sandfarmer.getResult().getItemMeta().getDisplayName(), new SandFarmer());
    }

    @Override
    public AutomaticFarmer getFarmer(ItemStack itemStack) {
        if (itemStack.getItemMeta() == null || itemStack.getItemMeta().getDisplayName() == null) {
            return null;
        }
        return this.automaticFarmerMap.get(ChatUtil.fixColor(itemStack.getItemMeta().getDisplayName()));
    }

    @Override
    public void registerFarm(String name, AutomaticFarmer automaticFarmer) {
        this.automaticFarmerMap.put(name, automaticFarmer);
        this.plugin.getLogger().log(Level.INFO, "Registered automaticfarmer check for title " + name);
    }

}
