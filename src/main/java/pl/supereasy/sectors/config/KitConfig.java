package pl.supereasy.sectors.config;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.config.api.Configurable;
import pl.supereasy.sectors.core.kits.data.Kit;
import pl.supereasy.sectors.core.specialitems.types.SpecialItemType;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.util.EnchantManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KitConfig extends Configurable {

    private static final SectorPlugin plugin = SectorPlugin.getInstance();
    public KitConfig() {
        super("kits.yml", "/home/SpigotSectors/");
    }

    @Override
    public void loadConfig() {
        FileConfiguration config = getConfig();
        for(String key : config.getConfigurationSection("kits").getKeys(false)){
            ConfigurationSection section = config.getConfigurationSection("kits." + key);

            final List<ItemStack> items = new ArrayList<>();
            for(String s : section.getStringList("items")){
                items.add(itemConvert(s));
            }
            final int seconds = section.getInt("time");
            final boolean enable = section.getBoolean("enable");
            final int timekey = section.getInt("timekey");
            final Material guiItem = Material.getMaterial(section.getString("guiItem"));
            final int guiIndex = section.getInt("guiIndex");
            final String guiTitle = section.getString("guiTitle");
            final List<String> guiLore = section.getStringList("guiLore");
            final UserGroup group = UserGroup.valueOf(section.getString("group"));
            plugin.getKitManager().getKits().put(key, new Kit(key, items, seconds, enable, timekey,guiItem,guiIndex,guiTitle,guiLore,group));
        }
    }
    //id-data-amount-enchant=enchantlevel;enchant=enchantlevel
    private ItemStack itemConvert(String item){
        String[] array = item.split("-");
        final int id = Integer.parseInt(array[0]);
        final short data = Short.parseShort(array[1]);
        final int amount = Integer.parseInt(array[2]);
        if(id == 54){
            final ItemStack itemStack = plugin.getCaseManager().getCases().get("PREMIUMCASE").getCaseItem().clone();
            itemStack.setAmount(amount);
            return itemStack;
        }
        if(id == 1008){
            return plugin.getSpecialItemManager().getSpecialItems().get(SpecialItemType.SPECIALTNT).getItemWithOption(amount);
        }
        final HashMap<Enchantment, Integer> enchants = new HashMap<>();

        ItemStack itemStack = new ItemStack(id,amount,data);
        if(array.length > 3) {
            for (String enchantments : array[3].split(";")) {
                String[] array2 = enchantments.split("=");
                if(array2.length > 1) {
                    enchants.put(EnchantManager.get(array2[0]), Integer.parseInt(array2[1]));
                }
            }
            itemStack.addUnsafeEnchantments(enchants);
        }
        return itemStack;
    }
}
