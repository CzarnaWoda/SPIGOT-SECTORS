package pl.supereasy.sectors.core.enchant;

import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.List;

public interface CustomEnchantStorage {

    List<CustomEnchant> getCustomEnchants();

    CustomEnchant getCustomEnchant(Enchantment enchantment);



}
