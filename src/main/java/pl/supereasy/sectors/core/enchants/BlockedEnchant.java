package pl.supereasy.sectors.core.enchants;

import org.bukkit.enchantments.Enchantment;

public class BlockedEnchant {

    private final Enchantment enchantment;
    private final int enchantLevel;

    public BlockedEnchant(Enchantment enchantment, int enchantLevel){
        this.enchantment = enchantment;
        this.enchantLevel = enchantLevel;
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public int getEnchantLevel() {
        return enchantLevel;
    }
}
