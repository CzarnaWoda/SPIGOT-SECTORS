package pl.supereasy.sectors.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.specialitems.types.SpecialItemType;

public class EasyUtil {

    private final static SectorPlugin plugin = SectorPlugin.getInstance();
    //This types of utils are not for any server - only for supereasy :O

    public static void convertStringItem(String input){
        final String[] array = input.split("-");
        final int rid = Integer.parseInt(array[0]);
        final int ramount = Integer.parseInt(array[1]);
        final short rdata = Short.parseShort(array[2]);
        final ItemStack itemStack = getItem(rid,ramount,rdata);
        if(array.length >= 4){
            String[] enchantarray = array[3].split(";");
            for(String enchants : enchantarray) {
                final String[] enchant = enchants.split("=");
                itemStack.addUnsafeEnchantment(EnchantManager.get(enchant[0]), Integer.parseInt(enchant[1]));
            }
        }
    }
    public static ItemStack getItem(int id, int amount, short data){
        ItemStack itemStack = null;
        if(amount < 0){
            return itemStack;
        }
        switch (id){
            default: {
                if(Material.getMaterial(id) == null){
                    return itemStack;
                }
                itemStack = new ItemStack(Material.getMaterial(id), amount, data);
                break;
            }
            case 54: {
                itemStack = plugin.getCaseManager().getCases().get("PREMIUMCASE").getCaseItem().clone();
                itemStack.setAmount(amount);
                break;
            }
            case 1001: {
                itemStack = plugin.getCraftingManager().getCraftingRecipes().get("GENERATOR").getResult().clone();
                itemStack.setAmount(amount);
                break;
            }
            case 1002: {
                itemStack = plugin.getCraftingManager().getCraftingRecipes().get("SANDFARMER").getResult().clone();
                itemStack.setAmount(amount);
                break;
            }
            case 1003: {
                itemStack = plugin.getCraftingManager().getCraftingRecipes().get("KOPACZFOSY").getResult().clone();
                itemStack.setAmount(amount);
                break;
            }
            case 1004: {
                itemStack = plugin.getCraftingManager().getCraftingRecipes().get("BOYFARMER").getResult().clone();
                itemStack.setAmount(amount);
                break;
            }
            case 1005: {
                itemStack = plugin.getSpecialItemManager().getSpecialItems().get(SpecialItemType.MONEYVOUCHER).getItemWithOption(data).clone();
                itemStack.setAmount(amount);
                break;
            }
            case 1006:{
                itemStack = plugin.getSpecialItemManager().getSpecialItems().get(SpecialItemType.RANKVOUCHER).getItemWithOption(data);
                itemStack.setAmount(amount);
                break;
            }
            case 1007:{
                itemStack = plugin.getSpecialItemManager().getSpecialItems().get(SpecialItemType.PIECE).getItemWithOption(data).clone();
                itemStack.setAmount(amount);
                break;
            }
            case 1008:{
                itemStack = plugin.getSpecialItemManager().getSpecialItems().get(SpecialItemType.SPECIALTNT).getItemBuilder().build().clone();
                itemStack.setAmount(amount);
                break;
            }
            case 1009:{
                itemStack = plugin.getSpecialItemManager().getSpecialItems().get(SpecialItemType.MAGICSPADE).getItemWithOption(amount).clone();
                break;
            }
            case 1010:{
                itemStack = plugin.getSpecialItemManager().getSpecialItems().get(SpecialItemType.COBBLEX).getItemWithOption(amount).clone();
                break;
            }
        }
        return itemStack;
    }
}
