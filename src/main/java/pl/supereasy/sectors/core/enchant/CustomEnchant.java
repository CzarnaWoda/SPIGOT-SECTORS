package pl.supereasy.sectors.core.enchant;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.enchantments.Enchantment;

@RequiredArgsConstructor
@Data
public class CustomEnchant {

    private final Enchantment enchantment;
    private final int enchantmentCost;
    private final int enchantmentLevels;
    private final int enchantmentBooks;
}
