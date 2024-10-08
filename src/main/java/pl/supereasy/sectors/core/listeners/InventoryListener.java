package pl.supereasy.sectors.core.listeners;

import org.bukkit.DyeColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

public class InventoryListener implements Listener {
    private ItemStack lapis;

    public InventoryListener(){
        Dye d = new Dye();
        d.setColor(DyeColor.BLUE);
        this.lapis = d.toItemStack();
        this.lapis.setAmount(3);
    }
    @EventHandler
    public void onEnchantOpen(InventoryOpenEvent e){
        if(e.getInventory() instanceof EnchantingInventory){
            e.getInventory().setItem(1,lapis);
        }
    }
    @EventHandler
    public void onEnchantClose(InventoryCloseEvent e){
        if ((e.getInventory() instanceof EnchantingInventory)) {
            e.getInventory().setItem(1, null);
        }
    }
    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent e)
    {
        if ((e.getClickedInventory() instanceof EnchantingInventory)) {
            if(e.getSlot() == 1){
                e.setCancelled(true);
            }
        }
        }
    @EventHandler
    public void enchantItemEvent(EnchantItemEvent e)
    {
            e.getInventory().setItem(1, this.lapis);
    }
}
