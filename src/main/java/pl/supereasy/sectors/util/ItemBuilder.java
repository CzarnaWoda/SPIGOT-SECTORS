package pl.supereasy.sectors.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.Potion;

public class ItemBuilder {
    private Material mat;
    private int amount;
    private short data;
    private String title;
    private final List<String> lore;
    private final HashMap<Enchantment, Integer> enchants;
    private Color color;
    private Potion potion;
    private final List<ItemFlag> itemFlags;

    public ItemBuilder(Material mat) {
        this(mat, 1);
    }

    public ItemBuilder(Material mat, int amount) {
        this(mat, amount, (short)0);
    }

    public ItemBuilder(Material mat, short data) {
        this(mat, 1, data);
    }

    public ItemBuilder(Material mat, int amount, short data) {
        this.title = null;
        this.lore = new ArrayList();
        this.enchants = new HashMap();
        this.itemFlags = new ArrayList<>();
        this.mat = mat;
        this.amount = amount;
        this.data = data;
    }

    public ItemBuilder setType(Material mat) {
        this.mat = mat;
        return this;
    }

    public ItemBuilder setTitle(String title) {
        this.title = ChatUtil.fixColor(title);
        return this;
    }
    public ItemBuilder addFlag(ItemFlag flag){
        this.itemFlags.add(flag);
        return this;
    }
    public ItemBuilder addFlags(List<ItemFlag> flags){
        this.itemFlags.addAll(flags);
        return this;
    }
    public ItemBuilder addLores(List<String> lores) {
        this.lore.addAll(lores);
        return this;
    }

    public ItemBuilder addLore(String lore) {
        this.lore.add(ChatUtil.fixColor(lore));
        return this;
    }

    public ItemBuilder setData(short data) {
        this.data = data;
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchant, int level) {
        this.enchants.remove(enchant);

        this.enchants.put(enchant, level);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment enchant) {
        this.enchants.remove(enchant);
        return this;
    }
    public ItemBuilder setColor(Color color) {
        if (!this.mat.name().contains("LEATHER_")) {
            throw new IllegalArgumentException("Can only dye leather armor!");
        } else {
            this.color = color;
            return this;
        }
    }

    public ItemBuilder setPotion(Potion potion) {
        if (this.mat != Material.POTION) {
            this.mat = Material.POTION;
        }

        this.potion = potion;
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemStack build() {
        Material mat = this.mat;
        if (mat == null) {
            mat = Material.AIR;
            Bukkit.getLogger().warning("Null material!");
        }

        ItemStack item = new ItemStack(this.mat, this.amount, this.data);
        ItemMeta meta = item.getItemMeta();
        if (this.title != null) {
            meta.setDisplayName(this.title);
        }

        if (!this.lore.isEmpty()) {
            meta.setLore(this.lore);
        }

        if (meta instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta)meta).setColor(this.color);
        }
        if(this.itemFlags.size() >= 1){
            itemFlags.forEach(meta::addItemFlags);
        }

        item.setItemMeta(meta);
        item.addUnsafeEnchantments(this.enchants);
        if (this.potion != null) {
            this.potion.apply(item);
        }

        return item;
    }

    public ItemBuilder clone() {
        ItemBuilder newBuilder = new ItemBuilder(this.mat);
        newBuilder.setTitle(this.title);
        Iterator var3 = this.lore.iterator();

        while(var3.hasNext()) {
            String lore = (String)var3.next();
            newBuilder.addLore(lore);
        }

        var3 = this.enchants.entrySet().iterator();

        while(var3.hasNext()) {
            Entry<Enchantment, Integer> entry = (Entry)var3.next();
            newBuilder.addEnchantment(entry.getKey(), entry.getValue());
        }

        newBuilder.setColor(this.color);
        newBuilder.potion = this.potion;
        return newBuilder;
    }

    public Material getType() {
        return this.mat;
    }

    public String getTitle() {
        return this.title;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public Color getColor() {
        return this.color;
    }

    public boolean hasEnchantment(Enchantment enchant) {
        return this.enchants.containsKey(enchant);
    }

    public int getEnchantmentLevel(Enchantment enchant) {
        return this.enchants.get(enchant);
    }

    public HashMap<Enchantment, Integer> getAllEnchantments() {
        return this.enchants;
    }

    public boolean isItem(ItemStack item) {
        return this.isItem(item, false);
    }

    public boolean isItem(ItemStack item, boolean strictDataMatch) {
        ItemMeta meta = item.getItemMeta();
        if (item.getType() != this.getType()) {
            return false;
        } else if (!meta.hasDisplayName() && this.getTitle() != null) {
            return false;
        } else if (!meta.getDisplayName().equals(this.getTitle())) {
            return false;
        } else if (!meta.hasLore() && !this.getLore().isEmpty()) {
            return false;
        } else {
            Iterator var5;
            if (meta.hasLore()) {
                var5 = meta.getLore().iterator();

                while(var5.hasNext()) {
                    String lore = (String)var5.next();
                    if (!this.getLore().contains(lore)) {
                        return false;
                    }
                }
            }

            var5 = item.getEnchantments().keySet().iterator();
            while(var5.hasNext()) {
                Enchantment enchant = (Enchantment)var5.next();
                if (!this.hasEnchantment(enchant)) {
                    return false;
                }
            }

            return true;
        }
    }
}
