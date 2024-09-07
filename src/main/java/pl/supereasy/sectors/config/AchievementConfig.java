package pl.supereasy.sectors.config;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.config.api.Configurable;
import pl.supereasy.sectors.core.achievements.data.Achievement;
import pl.supereasy.sectors.core.achievements.enums.AchievementType;
import pl.supereasy.sectors.util.EnchantManager;

import java.util.ArrayList;
import java.util.List;

public class AchievementConfig extends Configurable {

    private final SectorPlugin plugin = SectorPlugin.getInstance();

    private String guiName;
    private String itemName;
    private List<String> lore_NotFinished;
    private List<String> lore_During;
    private List<String> lore_Finished;
    public AchievementConfig() {
        super("achievement.yml", "/home/SpigotSectors/");
    }

    @Override
    public void loadConfig() {
        final FileConfiguration config = getConfig();

        for(String key : config.getConfigurationSection("achievements").getKeys(false)){
            final ConfigurationSection section = config.getConfigurationSection("achievements." + key);

            final AchievementType type = AchievementType.valueOf(section.getString("type"));
            final int amount = section.getInt("amount");
            List<ItemStack> rewards = new ArrayList<>();
            for(String items : section.getStringList("rewards")){
                final String[] array = items.split("-");
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
                rewards.add(itemStack);
            }
            final int moneyReward = section.getInt("moneyreward");
            final String achievementName = section.getString("name");
            plugin.getAchievementManager().createAchievement(new Achievement(key,amount,type,rewards,moneyReward,achievementName));

            loadVariables(config);
        }
    }
    private void loadVariables(FileConfiguration configuration){
        guiName = configuration.getString("gui.name");
        itemName = configuration.getString("gui.itemname");
        lore_NotFinished = configuration.getStringList("gui.lore.notfinished");
        lore_Finished = configuration.getStringList("gui.lore.finished");
        lore_During = configuration.getStringList("gui.lore.during");
    }


    //getters

    public List<String> getLore_During() {
        return lore_During;
    }

    public List<String> getLore_Finished() {
        return lore_Finished;
    }

    public List<String> getLore_NotFinished() {
        return lore_NotFinished;
    }

    public String getGuiName() {
        return guiName;
    }

    public String getItemName() {
        return itemName;
    }
}
