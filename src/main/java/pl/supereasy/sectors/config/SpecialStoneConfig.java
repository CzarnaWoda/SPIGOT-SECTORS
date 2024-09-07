package pl.supereasy.sectors.config;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.config.api.Configurable;
import pl.supereasy.sectors.util.EnchantManager;

import java.util.ArrayList;
import java.util.List;

public class SpecialStoneConfig extends Configurable {
    public SpecialStoneConfig() {
        super("cobblex.yml", "/home/SpigotSectors/");
    }

    private final SectorPlugin plugin = SectorPlugin.getInstance();
    private static final List<ItemStack> items = new ArrayList<>();


    @Override
    public void loadConfig() {
        final FileConfiguration config = getConfig();

        for(String key : config.getConfigurationSection("cobblex.rewards").getKeys(false)){

            //ITEM
            final ConfigurationSection section = config.getConfigurationSection("cobblex.rewards." + key);
            final String[] array = section.getString("item").split("-");
            final int rid = Integer.parseInt(array[0]);
            final int ramount = Integer.parseInt(array[1]);
            final short rdata = Short.parseShort(array[2]);
            final ItemStack itemStack;
            if(rid == 54){
                itemStack = plugin.getCaseManager().getCases().get("PREMIUMCASE").getCaseItem().clone();
                itemStack.setAmount(ramount);
            }else{
                itemStack = new ItemStack(Material.getMaterial(rid),ramount,rdata);
            }
            if(array.length >= 4){
                String[] enchantarray = array[3].split(";");
                for(String enchants : enchantarray) {
                    final String[] enchant = enchants.split("=");
                    itemStack.addUnsafeEnchantment(EnchantManager.get(enchant[0]), Integer.parseInt(enchant[1]));
                }
            }
            items.add(itemStack);
        }
    }

    public static List<ItemStack> getItems() {
        return items;
    }
}
