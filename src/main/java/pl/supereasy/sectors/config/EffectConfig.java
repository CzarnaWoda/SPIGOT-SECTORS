package pl.supereasy.sectors.config;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.config.api.Configurable;
import pl.supereasy.sectors.core.effects.Effect;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class EffectConfig extends Configurable {

    public EffectConfig() {
        super("effects.yml", "/home/SpigotSectors/");
    }

    @Override
    public void loadConfig() {
        SectorPlugin.getInstance().getLogger().log(Level.INFO, "Loading effects.yml");
        for (String s : SectorPlugin.getInstance().getEffectConfig().getConfigurationSection("effects").getKeys(false)) {
            final ConfigurationSection section = getConfigurationSection("effects." + s);


            final String guiName = ChatUtil.fixColor(section.getString("guiname"));
            final List<String> guiLore = new ArrayList<>();
            for(String l : section.getStringList("guilore")){
                guiLore.add(ChatUtil.fixColor(l));
            }
            final Material guiItem = Material.getMaterial(section.getString("guiitem"));
            final short data = (short) section.getInt("guiitemdata");
            final List<PotionEffect> potionEffects = new ArrayList<>();
            final int index = section.getInt("index");
            for(String pe : section.getStringList("potioneffects")){
                String[] array = pe.split(";");

                final PotionEffect potionEffect = new PotionEffect(PotionEffectType.getByName(array[0]),20*Integer.parseInt(array[1]),Integer.parseInt(array[2]));
                potionEffects.add(potionEffect);
            }
            final List<ItemStack> effectCost = ItemUtil.getItems(section.getString("effectcost"),1);

            SectorPlugin.getInstance().getEffectManager().registerEffect(new Effect(guiName,guiLore,guiItem,data,potionEffects,effectCost,index));
        }
    }
}
