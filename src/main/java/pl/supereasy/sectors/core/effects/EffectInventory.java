package pl.supereasy.sectors.core.effects;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.core.colors.HexColor;
import pl.supereasy.sectors.core.colors.HexColors;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemBuilder;
import pl.supereasy.sectors.util.ItemUtil;

import java.util.Collections;

public class EffectInventory {

    public static void openMenu(Player player){

        final InventoryGUI inv = new InventoryGUI(ChatUtil.fixColor("&8->> " + HexColor.DARK_GREEN2.getAsHex() + "EFEKTY DLA GRACZY&8 <<-"),6);

        final ItemBuilder greens = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)5).setTitle(ChatUtil.fixColor(ChatColor.DARK_PURPLE + "#"));
        final ItemBuilder blues = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)3).setTitle(ChatUtil.fixColor(ChatColor.BLUE + "#"));
        final ItemBuilder grays = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)0).setTitle(ChatUtil.fixColor(ChatColor.LIGHT_PURPLE + "#"));

        int[] green = {0,1,3,5,7,8,9,17,18,26,27,35,36,37,39,41,43,44,45,47,49,51,53,31};
        int[] gray = {2,4,6,10,12,14,16,19,21,23,25,28,30,32,34};
        int[] blue = {29,33,46,48,50,52};

        for(int i : green){
            inv.setItem(i,greens.build(),null);
        }
        for(int i : gray){
            inv.setItem(i,grays.build(),null);
        }
        for(int i : blue){
            inv.setItem(i,blues.build(),null);
        }

        int index = 0;
        for(Effect effect : SectorPlugin.getInstance().getEffectManager().getEffects()){
            inv.setItem(effect.getIndex(),effect.getItemBuilder().build(),(p, paramInventory, paramInt, paramItemStack) -> {
                if(ItemUtil.checkItems(effect.getEffectCost(),p)){
                    ItemUtil.removeItems(effect.getEffectCost(),p);
                    for(PotionEffect potionEffect : effect.getPotionEffects()){
                        p.addPotionEffect(potionEffect,true);
                    }
                }else{
                    ChatUtil.sendMessage(p,"&4Blad: &cNie posiadasz wymaganych przedmiotow " + ItemUtil.getItems(effect.getEffectCost()));
                }
            });
            index++;
        }
        final ItemBuilder milk = new ItemBuilder(Material.MILK_BUCKET).setTitle(ChatUtil.fixColor("&8->> " + HexColor.LIGHT_BLUE1.getAsHex() + "WYCZYSC EFEKTY &8<<-"))
                        .addLore("&8  * " + HexColor.LIGHT_RED.getAsHex() + "Kliknij aby wyczyscic wszystkie efekty !")
                                .addLore("&8  * " + HexColor.MEDIUM_YELLOW.getAsHex() + "KOSZT: " + HexColor.MEDIUM_GREEN1.getAsHex() + "16 BLOKOW EMERALDOW");
        inv.setItem(40,milk.build(),(paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            ItemStack item = new ItemStack(Material.EMERALD_BLOCK,16);
            if(ItemUtil.checkItems(Collections.singletonList(item),paramPlayer)){
                ItemUtil.removeItems(Collections.singletonList(item),paramPlayer);
                for(PotionEffect potionEffect : paramPlayer.getActivePotionEffects()){
                    paramPlayer.removePotionEffect(potionEffect.getType());
                }

            }else{
                ChatUtil.sendMessage(paramPlayer,"&4Blad: &cNie posiadasz wymaganych przedmiotow " + ItemUtil.getItems(Collections.singletonList(item)));
            }
        });
        inv.openInventory(player);
    }
}
