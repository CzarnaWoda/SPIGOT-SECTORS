package pl.supereasy.sectors.config;

import org.bukkit.configuration.ConfigurationSection;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.config.api.Configurable;
import pl.supereasy.sectors.core.textcommands.data.TextCommand;

import java.util.List;

public class TextCommandConfig extends Configurable {

    public TextCommandConfig() {
        super("textcommands.yml", "/home/SpigotSectors/");
    }

    @Override
    public void loadConfig() {
        for (String key : getConfigurationSection("textcommands").getKeys(false)) {
            final ConfigurationSection section = getConfigurationSection("textcommands." + key);
            final List<String> aliases = section.getStringList("aliases");
            final List<String> text = section.getStringList("text");

            SectorPlugin.getInstance().getTextCommandManager().addTextCommand(new TextCommand(key, aliases, text));
        }
    }
}
