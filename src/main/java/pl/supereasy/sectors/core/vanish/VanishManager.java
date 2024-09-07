package pl.supereasy.sectors.core.vanish;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;

import java.util.List;

@RequiredArgsConstructor
public class VanishManager {

    private final SectorPlugin plugin;
    private final List<Player> vanished;

    public boolean isVanished(Player player){
        return vanished.contains(player);
    }

    public boolean toggleVanish(Player player){
        if(isVanished(player)){
            removeVanishFromPlayer(player);
        }else{
            applyVanishToPlayer(player);
        }
        return isVanished(player);
    }
    public void applyVanishToPlayer(Player player){
        vanished.add(player);
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            final User user = plugin.getUserManager().getUser(onlinePlayer.getUniqueId());
            if(!user.getGroup().hasPermission(UserGroup.HELPER)) {
                onlinePlayer.hidePlayer(player);
            }
        });
    }
    private void removeVanishFromPlayer(Player player){
        vanished.remove(player);
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            final User user = plugin.getUserManager().getUser(onlinePlayer.getUniqueId());
            if(!user.getGroup().hasPermission(UserGroup.HELPER)) {
                onlinePlayer.showPlayer(player);
            }
        });
    }

    public List<Player> getVanished() {
        return vanished;
    }
}
