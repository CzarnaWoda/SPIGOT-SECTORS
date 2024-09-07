package pl.supereasy.sectors.config;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.config.api.Configurable;
import pl.supereasy.sectors.core.craftings.api.CraftingSlot;
import pl.supereasy.sectors.core.craftings.impl.CraftingSlotImpl;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemBuilder;

import java.util.ArrayList;
import java.util.List;

public class CraftingConfig extends Configurable {


    public CraftingConfig() {
        super("crafting.yml", "/home/SpigotSectors/");
    }

    @Override
    public void loadConfig() {
        for (String key : getConfig().getConfigurationSection("craftings").getKeys(false)) {
            ConfigurationSection section = getConfigurationSection("craftings." + key);
            List<CraftingSlot> craftingSlots = new ArrayList<>();
            for (String slots : getConfigurationSection("craftings." + key + ".slots").getKeys(false)) {
                ConfigurationSection section1 = getConfigurationSection("craftings." + key + ".slots." + slots);
                final int slot = Integer.parseInt(slots);
                final Material material = Material.getMaterial(section1.getString("item"));
                final int amount = section1.getInt("amount");
                craftingSlots.add(new CraftingSlotImpl(slot,material,amount));
            }
            final ItemBuilder result = new ItemBuilder(Material.getMaterial(section.getString("item")),section.getInt("amount")).setTitle(ChatUtil.fixColor(section.getString("name")));
            section.getStringList("lore").forEach(lore -> result.addLore(ChatUtil.fixColor(lore)));
            SectorPlugin.getInstance().getCraftingManager().registerRecipe(key, result.build(), craftingSlots, section.getString("autocrafting"));

        }

    }
}
