package pl.supereasy.sectors.core.craftings.inventory;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.core.colors.HexColors;
import pl.supereasy.sectors.core.craftings.api.CraftingRecipe;
import pl.supereasy.sectors.core.craftings.api.CraftingSlot;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemBuilder;
import pl.supereasy.sectors.util.ItemUtil;

import java.util.Collections;
import java.util.List;

public class CraftingInventory extends HexColors {



    public static void openInventory(Player player){
        final InventoryGUI inv = new InventoryGUI(ChatUtil.fixColor("&8->>( " + getHexAsColor("#1bc4a3") + "E" + getHexAsColor("#1bc4b3") + "N" + getHexAsColor("#1bc4c4") + "I" + getHexAsColor("#1bb9c4") + "U" + getHexAsColor("#1badc4") + "M" +  getHexAsColor("#1b97c4") + "C" + "&8 )<<-"), 6);

        final ItemBuilder blue = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)11);
        final ItemBuilder cyan = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)9);
        final ItemBuilder orange = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)1);
        final ItemBuilder black = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)15);
        final ItemBuilder purple = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)10);

        inv.setItem(0,blue.build(),null);
        inv.setItem(1,blue.build(),null);
        inv.setItem(2,black.build(),null);
        inv.setItem(6,black.build(),null);
        inv.setItem(7,blue.build(),null);
        inv.setItem(8,blue.build(),null);
        inv.setItem(9,blue.build(),null);
        inv.setItem(10,orange.build(),null);
        inv.setItem(11,blue.build(),null);
        inv.setItem(15,blue.build(),null);
        inv.setItem(17,blue.build(),null);
        inv.setItem(18,black.build(),null);
        inv.setItem(19,blue.build(),null);
        inv.setItem(20,blue.build(),null);
        inv.setItem(24,blue.build(),null);
        inv.setItem(26,black.build(),null);
        inv.setItem(27,cyan.build(),null);
        inv.setItem(28,blue.build(),null);
        inv.setItem(29,purple.build(),null);
        inv.setItem(30,black.build(),null);
        inv.setItem(31,black.build(),null);
        inv.setItem(32,black.build(),null);
        inv.setItem(33,purple.build(),null);
        inv.setItem(34,blue.build(),null);
        inv.setItem(35,cyan.build(),null);
        inv.setItem(36, cyan.build(), null);
        inv.setItem(37, cyan.build(), null);
        inv.setItem(38, blue.build(), null);
        inv.setItem(39, black.build(), null);
        inv.setItem(40, black.build(), null);
        inv.setItem(41, black.build(), null);
        inv.setItem(42, blue.build(), null);
        inv.setItem(43, cyan.build(), null);
        inv.setItem(44, cyan.build(), null);
        inv.setItem(45, blue.build(), null);
        inv.setItem(53, blue.build(), null);
        setCrafting(inv, SectorPlugin.getInstance().getCraftingManager().getCraftingRecipeList().get(0));

        inv.openInventory(player);
    }
    private static void setCrafting(InventoryGUI inv, CraftingRecipe craftingRecipe){
        inv.setItem(16, craftingRecipe.getResult(),null);
        int[] cslots = {46,47,48,49,50,51,52};
        int indexx = 0;
        final ItemBuilder gray = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)7).setTitle(ChatUtil.fixColor("&6#"));
        for (int i : cslots) {
            CraftingRecipe recipe = null;
            if (SectorPlugin.getInstance().getCraftingManager().getCraftingRecipeList().size() >= indexx + 1) {
                recipe = SectorPlugin.getInstance().getCraftingManager().getCraftingRecipeList().get(indexx);
            }
            if (recipe != null) {
                CraftingRecipe finalRecipe = recipe;
                inv.setItem(i, recipe.getResult(), (p, inventory, slot, item) -> {
                    setCrafting(inv, finalRecipe);
                });
            } else {
                inv.setItem(i, gray.build(), null);
            }
            indexx++;
        }
        int[] slots = {3,4,5,12,13,14,21,22,23};
        int index = 0;
        for(CraftingSlot craftingSlot : craftingRecipe.getSlots()){
            final ItemBuilder itemBuilder = new ItemBuilder(craftingSlot.getMaterial(), craftingSlot.getAmount());
            inv.setItem(slots[index], itemBuilder.build(),null);
            index++;
        }
        int[] craftings = {46,47,48,49,50,51,52};
        for(int i : craftings){
            if(inv.getItem(i) != null && inv.getItem(i).equals(craftingRecipe.getResult())){
                ItemStack item = inv.getItem(i);
                ItemMeta meta = item.getItemMeta();
                meta.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.setItemMeta(meta);
            }
        }
        ItemBuilder craftingtable = new ItemBuilder(Material.WORKBENCH,1).setTitle(ChatUtil.fixColor("&8->> ( &aAutomatyczny Crafting &8) <<-"))
                .addLore(ChatUtil.fixColor("&8  |->> &7Kliknij aby &6&bautomatycznie utworzyc &2&n" + craftingRecipe.getResult().getItemMeta().getDisplayName()));
        List<ItemStack> items = ItemUtil.getItems(craftingRecipe.getAutoCrafting(),1);
        inv.setItem(25,craftingtable.build(),(p, paramInventory, paramInt, paramItemStack) -> {
            if(ItemUtil.checkAndRemove(items, p)){
                ItemUtil.giveItems(Collections.singletonList(craftingRecipe.getResult()),p);
                return;
            }
            ChatUtil.sendMessage(p,"&4Blad: &cNie posiadasz wymaganych przedmiotow &8(&4" + ItemUtil.getItems(items) + "&8)");
        });
    }
}
