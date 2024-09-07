package pl.supereasy.sectors.config;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.config.api.Configurable;
import pl.supereasy.sectors.core.boss.data.BossReward;
import pl.supereasy.sectors.core.shops.data.Sell;
import pl.supereasy.sectors.util.EnchantManager;

public class BossConfig extends Configurable {
    private final static SectorPlugin plugin = SectorPlugin.getInstance();
    public BossConfig() {
        super("boss.yml", "/home/SectorPlugin/");
    }

    //VARS

    public static String BOSS_NAME;
    public static int BOSS_HEALTH;

    @Override
    public void loadConfig() {
        final FileConfiguration config = getConfig();

        BOSS_NAME = config.getString("boss.name");
        BOSS_HEALTH = config.getInt("boss.health");

        for(String key : config.getConfigurationSection("rewards").getKeys(false)){
            final ConfigurationSection section = config.getConfigurationSection("rewards." + key);

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
            final BossReward bossReward = new BossReward(key,itemStack);
            plugin.getBossManager().addReward(bossReward);
        }
    }
}
