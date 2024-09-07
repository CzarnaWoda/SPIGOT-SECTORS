package pl.supereasy.sectors.core.shops.data;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.supereasy.sectors.config.ShopConfig;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemUtil;

import java.util.ArrayList;
import java.util.List;

public class Sell {

    private final String key;
    private final String itemName;
    private final ItemStack item;
    private final double price;
    private final int index;
    private final boolean evolution;

    public Sell(String key, String itemName, ItemStack itemStack, double price, final int index, boolean evolution) {
        this.key = key;
        this.itemName = itemName;
        this.item = itemStack;
        this.price = price;
        this.evolution = evolution;
        this.index = index;
    }

    public String getItemName() {
        return itemName;
    }

    public String getKey() {
        return key;
    }

    public double getPrice() {
        return price;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getIndex() {
        return index;
    }

    public ItemStack getGuiItem() {
        final ItemStack itemStack = getItem().clone();

        final ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatUtil.fixColor(ShopConfig.GUI_ITEMNAME.replace("{ITEM}", ItemUtil.getPolishMaterial(itemStack.getType()))
                .replace("{AMOUNT}", String.valueOf(itemStack.getAmount()))
                .replace("{ITEMNAME}",getItemName())));

        List<String> lores = new ArrayList<>();
        for (String lore : ShopConfig.GUI_LORE) {
            if(!lore.equalsIgnoreCase("{ENOUGHMONEY}")) {
                lores.add(ChatUtil.fixColor(lore.replace("{PRICE}", String.valueOf(getPrice()))));
            }
        }
        meta.setLore(lores);

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public boolean isEvolution() {
        return evolution;
    }
}
