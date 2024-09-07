package pl.supereasy.sectors.core.boss.data;

import org.bukkit.inventory.ItemStack;

public class BossReward {

    private final String key;
    private final ItemStack itemStack;

    public BossReward(String key, ItemStack itemStack){
        this.key = key;
        this.itemStack = itemStack;
    }

    public String getKey() {
        return key;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
