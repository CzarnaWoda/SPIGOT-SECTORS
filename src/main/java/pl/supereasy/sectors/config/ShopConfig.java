package pl.supereasy.sectors.config;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.config.api.Configurable;
import pl.supereasy.sectors.core.shops.data.Buy;
import pl.supereasy.sectors.core.shops.data.Sell;
import pl.supereasy.sectors.core.shops.enums.ShopCategory;
import pl.supereasy.sectors.util.EnchantManager;

import java.util.List;

public class ShopConfig extends Configurable {

    private static final SectorPlugin plugin = SectorPlugin.getInstance();

    public static String GUI_ITEMNAME;
    public static List<String> GUI_LORE;

    public ShopConfig() {
        super("shops.yml", "/home/SpigotSectors/");
    }

    @Override
    public void loadConfig() {
        final FileConfiguration config = getConfig();

        //vars
        GUI_ITEMNAME = config.getString("gui.itemname");
        GUI_LORE = config.getStringList("gui.lore");



        for(String key : config.getConfigurationSection("buy").getKeys(false)){
            final ConfigurationSection section = config.getConfigurationSection("buy." + key);

            //ITEM
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

            //declare
            final Buy buy = new Buy(key, section.getString("itemName"), itemStack, section.getDouble("price"), section.getInt("index"), ShopCategory.valueOf(section.getString("category")));
            plugin.getShopManager().addBuyItem(buy);
        }

        for(String key : config.getConfigurationSection("sell").getKeys(false)){
            final ConfigurationSection section = config.getConfigurationSection("sell." + key);

            //ITEM
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

            //declare
            final Sell sell = new Sell(key, section.getString("itemName"), itemStack, section.getDouble("price"), section.getInt("index"), section.getBoolean("evolution"));
            plugin.getShopManager().addSellItem(sell);
        }
    }
}
