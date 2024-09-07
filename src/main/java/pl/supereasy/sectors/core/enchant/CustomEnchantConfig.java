package pl.supereasy.sectors.core.enchant;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.config.api.Configurable;
import pl.supereasy.sectors.util.EnchantManager;

import java.util.ArrayList;
import java.util.List;

public class CustomEnchantConfig extends Configurable {

    public CustomEnchantConfig() {
        super("customenchant.yml", "/home/SpigotSectors/");
    }

    @Override
    public void loadConfig() {
        final FileConfiguration configuration = getConfig();

        List<CustomEnchant> customEnchants = new ArrayList<>();
        for(String key : configuration.getConfigurationSection("customEnchants").getKeys(false)){
            final ConfigurationSection section = getConfigurationSection("customEnchants." + key);

            final Enchantment enchantment = EnchantManager.getEnchantment(section.getString("enchantment"));
            final int enchantmentCost = section.getInt("enchantmentCost");
            final int enchantmentLevels = section.getInt("enchantmentLevels");
            final int enchantmentBooks = section.getInt("enchantmentBooks");

            final CustomEnchant customEnchant = new CustomEnchant(enchantment,enchantmentCost,enchantmentLevels,enchantmentBooks);

            customEnchants.add(customEnchant);
        }
        SectorPlugin.getInstance().setCustomEnchantStorage(new CustomEnchantStorageImpl(customEnchants));
    }
}
