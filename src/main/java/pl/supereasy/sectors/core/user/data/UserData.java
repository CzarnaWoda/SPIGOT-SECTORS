package pl.supereasy.sectors.core.user.data;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.redis.channels.RedisChannel;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.api.sql.api.Loadable;
import pl.supereasy.sectors.api.sql.api.Saveable;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.GsonUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class UserData implements Loadable, Saveable {

    private final SectorPlugin plugin;

    public UserData(SectorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadData() {
        final AtomicInteger ai = new AtomicInteger(0);
        RedisChannel.INSTANCE.USERS.forEach((uuid, s) -> {
            final User user = GsonUtil.fromJson(s, User.class);
            plugin.getUserManager().registerUser(user);
            ai.getAndIncrement();
        });
        plugin.getLogger().log(Level.INFO, "Zaladowano " + plugin.getUserManager().getUsers().size() + " userow z bazy!");
    }

    @Override
    public void saveData() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            final User user = this.plugin.getUserManager().getUser(p.getUniqueId());
            if (!user.saveUserToValues()) {
                this.plugin.getLogger().log(Level.WARNING, "Wystapil problem z zapisem danych gracza " + user.getName());
            }
            /*final Location loc = p.getLocation();
            ((CraftPlayer) p).getHandle().setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());*/
            user.insert(false);
        }
    }
}
