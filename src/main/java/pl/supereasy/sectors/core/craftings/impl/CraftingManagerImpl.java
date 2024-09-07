package pl.supereasy.sectors.core.craftings.impl;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.craftings.api.CraftingManager;
import pl.supereasy.sectors.core.craftings.api.CraftingRecipe;
import pl.supereasy.sectors.core.craftings.api.CraftingSlot;
import pl.supereasy.sectors.core.craftings.enums.CraftItem;

import java.util.*;
import java.util.logging.Level;

public class CraftingManagerImpl implements CraftingManager {

    private final SectorPlugin plugin;
    private final Map<String, CraftingRecipe> craftingRecipes;
    private final List<CraftingRecipe> craftingRecipeList;

    public CraftingManagerImpl(SectorPlugin plugin) {
        this.plugin = plugin;
        this.craftingRecipes = new HashMap<>();
        this.craftingRecipeList =  new ArrayList<>();
    }

    @Override
    public void registerRecipe(CraftingRecipe craftingRecipe) {
        if(craftingRecipes.containsKey(craftingRecipe.getName())){
            this.plugin.getLOGGER().log(Level.WARNING, "Recipe with name " + craftingRecipe.getName() + " exists!");
            return;
        }
        this.craftingRecipeList.add(craftingRecipe);
        this.craftingRecipes.put(craftingRecipe.getName(), craftingRecipe);

        final List<Character> characters = Arrays.asList('A','B','C','D','E','F','G','H','I');
        ShapedRecipe recipe = new ShapedRecipe(craftingRecipe.getResult());
        recipe.shape("ABC","DEF","GHI");
        for(int i = 0 ; i < 9; i ++){
            if(craftingRecipe.getSlots().get(i) != null && craftingRecipe.getSlots().get(i).getMaterial() != null && craftingRecipe.getSlots().get(i).getMaterial() != Material.AIR) {
                recipe.setIngredient(characters.get(i), craftingRecipe.getSlots().get(i).getMaterial());
            }else{
                recipe.setIngredient(characters.get(i), Material.AIR);
            }
        }
        Bukkit.addRecipe(recipe);
    }


    @Override
    public void registerRecipe(String name, ItemStack result, List<CraftingSlot> craftingSlotList, String autoCrafting) {
        if(craftingRecipes.containsKey(name)){
            this.plugin.getLOGGER().log(Level.WARNING, "Recipe with name " + name + " exists!");
            return;
        }
        final CraftingRecipe craftingRecipe = new CraftingRecipeImpl(name, result, craftingSlotList, autoCrafting);
        this.craftingRecipeList.add(craftingRecipe);
        this.craftingRecipes.put(name, craftingRecipe);
        final List<Character> characters = Arrays.asList('A','B','C','D','E','F','G','H','I');
        ShapedRecipe recipe = new ShapedRecipe(result);
        recipe.shape("ABC","DEF","GHI");
        for(int i = 0 ; i < 9; i ++){
            if(craftingSlotList.get(i) != null && craftingSlotList.get(i).getMaterial() != null && craftingSlotList.get(i).getMaterial() != Material.AIR) {
                recipe.setIngredient(characters.get(i), craftingSlotList.get(i).getMaterial());
            }else{
                recipe.setIngredient(characters.get(i), Material.AIR);
            }
        }
        Bukkit.addRecipe(recipe);
    }

    @Override
    public void unregisterRecipe(String name) {
        this.craftingRecipeList.remove(craftingRecipes.get(name));
        this.craftingRecipes.remove(name);
    }

    public Map<String, CraftingRecipe> getCraftingRecipes() {
        return craftingRecipes;
    }

    @Override
    public CraftingRecipe findRecipe(ItemStack itemStack) {
        for (CraftingRecipe recipe : this.craftingRecipes.values()) {
            if (recipe.getResult().isSimilar(itemStack)) {
                return recipe;
            }
        }
        return null;
    }

    @Override
    public CraftingRecipe getRecipe(String recipeName) {
        return this.craftingRecipes.get(recipeName);
    }

    @Override
    public CraftingRecipe getRecipe(CraftItem craftItem) {
        return this.craftingRecipes.get(craftItem.name());
    }

    public List<CraftingRecipe> getCraftingRecipeList() {
        return craftingRecipeList;
    }
}
