package pl.supereasy.sectors.core.home.impl;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.api.sql.api.Loadable;
import pl.supereasy.sectors.core.home.api.Home;
import pl.supereasy.sectors.core.user.impl.User;

import java.sql.ResultSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class HomeData implements Loadable {

    private final SectorPlugin plugin;

    public HomeData(SectorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadData() {
        //W userze jest mapa która wleci do redisa
        /*try {
            final ResultSet rs = this.plugin.getMySQL().query("SELECT * FROM `users_homes`");
            final AtomicInteger ai = new AtomicInteger(0);
            while (rs.next()) {
                final UUID uuid = UUID.fromString(rs.getString("uuid"));
                final User user = this.plugin.getUserManager().getUser(uuid);
                if (user == null) {
                    throw new UserNotFoundException("Nie znaleziono usera o UUID: " + uuid.toString());
                }
                final Sector sector = this.plugin.getSectorManager().getSector(rs.getString("sectorName"));
                final Home home = new HomeImpl(rs.getString("homeName"), new Location(Bukkit.getWorld("world"), rs.getInt("locX"),
                        rs.getInt("locY"),
                        rs.getInt("locZ")
                ), sector);
                user.registerHome(home);
                ai.incrementAndGet();
            }
            this.plugin.getLOGGER().log(Level.INFO, "Zaladowano " + ai.get() + " domow!");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
