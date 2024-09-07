package pl.supereasy.sectors.config;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.config.api.Configurable;
import pl.supereasy.sectors.proxies.Proxy;
import pl.supereasy.sectors.proxies.ProxyManager;

import java.io.File;
import java.util.logging.Level;

public class ProxiesConfig extends Configurable {

    public ProxiesConfig() {
        super("proxies.yml", "/home/SpigotSectors/");
    }

    @Override
    public void loadConfig() {
        for (String key : getConfig().getConfigurationSection("proxies").getKeys(false)) {
            final ConfigurationSection c = getConfigurationSection("proxies." + key);


            final String proxyName = c.getString("proxyname");

            final Proxy proxy = new Proxy(proxyName);

            ProxyManager.registerProxy(proxy);

            SectorPlugin.getInstance().getLogger().log(Level.INFO, "Zaladowalem obiekt proxy: " + proxyName);
        }
    }
}
