package pl.supereasy.sectors.core.enchant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.enchantments.Enchantment;

import java.util.List;

@RequiredArgsConstructor
public class CustomEnchantStorageImpl implements CustomEnchantStorage{

    private final List<CustomEnchant> customEnchants;

    @Override
    public List<CustomEnchant> getCustomEnchants() {
        return customEnchants;
    }

    @Override
    public CustomEnchant getCustomEnchant(Enchantment enchantment) {
        for(CustomEnchant customEnchant : customEnchants){
            if (customEnchant.getEnchantment().equals(enchantment)){
                return customEnchant;
            }
        }
        return null;
    }

}
