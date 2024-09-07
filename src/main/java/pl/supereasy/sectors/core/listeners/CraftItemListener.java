package pl.supereasy.sectors.core.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.craftings.api.CraftingRecipe;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CraftItemListener implements Listener {

    private final SectorPlugin plugin;
    private final List<Material> diamondItems = Arrays.asList(Material.DIAMOND_SWORD,Material.DIAMOND_AXE,Material.DIAMOND_BOOTS,Material.DIAMOND_LEGGINGS,Material.DIAMOND_CHESTPLATE,Material.DIAMOND_HELMET,Material.DIAMOND_HOE,Material.DIAMOND_SPADE);
    public CraftItemListener(SectorPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onCraftDiamondItem(CraftItemEvent e){
        if(diamondItems.contains(e.getRecipe().getResult().getType())){
            if(!plugin.getCoreConfig().isDiamondItems()){
                e.setCancelled(true);
                ChatUtil.sendMessage(e.getWhoClicked(), "&4&lUwaga, &cdiamentowe przedmioty sa aktualnie wylaczone");
            }
        }
    }
    @EventHandler
    public void onItemCraft(CraftItemEvent e){
        final CraftingInventory inv = e.getInventory();
        if (e.getSlotType() != InventoryType.SlotType.RESULT) {
            return;
        }
        for (CraftingRecipe recipe : plugin.getCraftingManager().getCraftingRecipeList()) {
            if (e.getCurrentItem() != null && e.getCurrentItem().isSimilar(recipe.getResult())) {
                e.setCancelled(true);
                for (int i = 1; i < 10; i++) {
                    if (inv.getItem(i).getAmount() < recipe.getSlots().get(i - 1).getAmount()) {
                        inv.setResult(new ItemStack(Material.AIR));
                        return;
                    }
                }
                for (int i = 1; i < 10; i++) {
                    if (inv.getItem(i).getAmount() == recipe.getSlots().get(i - 1).getAmount()) {
                        inv.setItem(i, new ItemStack(Material.AIR));
                    } else if (inv.getItem(i).getAmount() > recipe.getSlots().get(i - 1).getAmount()) {
                        inv.getItem(i).setAmount(inv.getItem(i).getAmount() - recipe.getSlots().get(i - 1).getAmount());
                    }
                }
                final ItemStack give = recipe.getResult();
                give.setAmount(1);
                ItemUtil.giveItems(Collections.singletonList(give), (Player) e.getWhoClicked());
            }
        }
    }


    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent e){
        for(CraftingRecipe recipe : plugin.getCraftingManager().getCraftingRecipeList()){
            if(e.getInventory().getResult() != null && e.getInventory().getResult().isSimilar(recipe.getResult())){
                for (int i = 1; i < 10; i++) {
                    if (e.getInventory().getItem(i).getAmount() < recipe.getSlots().get(i - 1).getAmount()) {
                        e.getInventory().setResult(new ItemStack(Material.AIR));
                        return;
                    }
                }
            }
        }
    }
}
