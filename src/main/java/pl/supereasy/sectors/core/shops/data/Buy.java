package pl.supereasy.sectors.core.shops.data;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.supereasy.sectors.config.ShopConfig;
import pl.supereasy.sectors.core.shops.enums.ShopCategory;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemUtil;
import pl.supereasy.sectors.util.MathUtil;

import java.util.ArrayList;
import java.util.List;

public class Buy {

    private final String key;
    private final String itemName;
    private final ItemStack item;
    private final int index;
    private final double price;
    private final ShopCategory category;

    public Buy(String key, String itemName, ItemStack itemStack, double price, final int index, ShopCategory category) {
        this.key = key;
        this.itemName = itemName;
        this.item = itemStack;
        this.price = price;
        this.index = index;
        this.category = category;
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

    public ShopCategory getCategory() {
        return category;
    }

    public int getIndex() {
        return index;
    }

    public ItemStack getGuiItem(double usermoney) {
        final ItemStack itemStack = getItem().clone();

        final ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatUtil.fixColor(ShopConfig.GUI_ITEMNAME.
                replace("{ITEM}", ItemUtil.getPolishMaterial(itemStack.getType()).replace("zlote buty","ANTY NOGI"))
                .replace("{AMOUNT}", String.valueOf(itemStack.getAmount())))
                .replace("{ITEMNAME}",getItemName()));

        List<String> lores = new ArrayList<>();
        for (String lore : ShopConfig.GUI_LORE) {
            lores.add(ChatUtil.fixColor(lore.replace("{PRICE}", String.valueOf(getPrice())).replace("{ENOUGHMONEY}",(usermoney >= price ? "" : "&8  * &7Aby zakupić ten przedmiot brakuje ci &c" + MathUtil.round((price - usermoney),2) + "§#f3fa37zł"))));
        }
        meta.setLore(lores);

        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
