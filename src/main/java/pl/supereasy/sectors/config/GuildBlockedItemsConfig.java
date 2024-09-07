package pl.supereasy.sectors.config;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.config.api.Configurable;
import pl.supereasy.sectors.core.drop.data.Drop;
import pl.supereasy.sectors.util.item.LimitedBlock;

import java.util.*;
import java.util.logging.Level;

public class GuildBlockedItemsConfig extends Configurable {

    private final Map<Material, Integer> limitedItems = new HashMap<>();

    public GuildBlockedItemsConfig() {
        super("guild_blocked_items.yml", "/home/SpigotSectors/");
    }

    @Override
    public void loadConfig() {
        final List<String> section = SectorPlugin.getInstance().getGuildBlockedItemsConfig().getConfig().getStringList("items");
        for (String s : section) {
            final String[] split = s.split(";");
            final Material material = Material.valueOf(split[0]);
            final int amount = Integer.parseInt(split[1]);
            limitedItems.put(material, amount);
            SectorPlugin.getInstance().getLogger().log(Level.INFO, "Loaded guild limited block " + material.name() + " to amount " + amount);
        }
    }

    public Map<Material, Integer> getLimitedItems() {
        return limitedItems;
    }
}
