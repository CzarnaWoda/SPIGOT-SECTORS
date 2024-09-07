package pl.supereasy.sectors.core.user.enderchests.impl;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.core.user.enderchests.api.EnderChest;
import pl.supereasy.sectors.util.ChatUtil;

public class EnderChestImpl implements EnderChest {

    private transient Inventory inventory;
    private ItemStack[] itemStacks;

    public EnderChestImpl() {
        this.inventory = Bukkit.createInventory(null, 72, ChatUtil.fixColor("&6EnderChest"));
        this.itemStacks = this.inventory.getContents();
    }

    public void setup() {
        this.inventory = Bukkit.createInventory(null, 72, ChatUtil.fixColor("&6EnderChest"));
        this.inventory.setContents(this.itemStacks);
    }

    public void save() {
        this.itemStacks = this.inventory.getContents();
    }

    @Override
    public ItemStack[] getContent() {
        return this.inventory.getContents();
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
}
