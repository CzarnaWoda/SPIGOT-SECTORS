package pl.supereasy.sectors.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.api.sectors.type.SectorType;
import pl.supereasy.sectors.config.api.Configurable;

public class SectorMapConfig extends Configurable {

    public SectorMapConfig() {
        super("sectormap.yml", "/home/SpigotSectors/");
    }

    @Override
    public void loadConfig() {
        for (String key : getConfig().getConfigurationSection("sectors").getKeys(false)) {
            final ConfigurationSection section = getConfigurationSection("sectors." + key);
            final SectorType sectorType = SectorType.valueOf(section.getString("sectorType"));
            final Sector sector = new Sector(section.getString("name"), section.getInt("maxX"),
                    section.getInt("maxZ"), section.getInt("minX"), section.getInt("minZ"),
                    section.getInt("offset"), sectorType, section.getInt("uniqueid"));
            SectorPlugin.getInstance().getSectorManager().registerSector(sector);
        }
    }
}
