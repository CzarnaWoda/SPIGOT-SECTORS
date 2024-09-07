package pl.supereasy.sectors.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.enchants.BlockedEnchant;
import pl.supereasy.sectors.util.ChatUtil;

public class EnchantListener implements Listener {

    private final SectorPlugin plugin;

    public EnchantListener(SectorPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onEnchantItem(EnchantItemEvent e){
        for(BlockedEnchant enchantBlock : plugin.getCoreConfig().blockedenchants.values()){
            if(e.getEnchantsToAdd().get(enchantBlock.getEnchantment()) != null && e.getEnchantsToAdd().get(enchantBlock.getEnchantment()) >= enchantBlock.getEnchantLevel()){
                e.getEnchantsToAdd().put(enchantBlock.getEnchantment(), (enchantBlock.getEnchantLevel()-1));
            }
        }
    }
    @EventHandler
    public void onAnvilUse(InventoryClickEvent e){
        if(e.getView().getType().equals(InventoryType.ANVIL)){
            final int rawSlot = e.getRawSlot();
            if(rawSlot == 2){
                final ItemStack itemStack = e.getCurrentItem();
                if(itemStack != null){
                    final ItemMeta itemMeta = itemStack.getItemMeta();
                    if(itemMeta != null){
                        for(BlockedEnchant enchantBlock : plugin.getCoreConfig().blockedenchants.values()){
                            if(itemMeta.getEnchants().get(enchantBlock.getEnchantment()) != null && itemMeta.getEnchants().get(enchantBlock.getEnchantment()) >= enchantBlock.getEnchantLevel()){
                                itemMeta.removeEnchant(enchantBlock.getEnchantment());
                                itemMeta.addEnchant(enchantBlock.getEnchantment(),enchantBlock.getEnchantLevel()-1,true);
                                itemStack.setItemMeta(itemMeta);
                                final Player player = (Player) e.getWhoClicked();
                                ChatUtil.sendMessage(player, "&4Blad: &cTen enchant jest zablokowany na serwerze! &8( &4" + enchantBlock.getEnchantment().getName() + "-" + enchantBlock.getEnchantLevel() + " &8)");
                            }
                        }
                    }
                }
            }
        }
    }
}
