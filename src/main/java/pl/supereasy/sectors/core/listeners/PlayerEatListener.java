package pl.supereasy.sectors.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.user.UserEatKoxPacket;
import pl.supereasy.sectors.api.packets.impl.user.UserEatRefPacket;
import pl.supereasy.sectors.core.user.impl.User;

import java.util.Arrays;
import java.util.List;

public class PlayerEatListener implements Listener {

    private final SectorPlugin plugin;

    public PlayerEatListener(SectorPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        if (e.getItem().getType().equals(Material.GOLDEN_APPLE) && e.getItem().getDurability() == 0) {
            addPotionEffects(Arrays.asList(new PotionEffect(PotionEffectType.ABSORPTION,120*20,1),new PotionEffect(PotionEffectType.REGENERATION,5*20,2)),e);
            final Packet packet = new UserEatRefPacket(e.getPlayer().getUniqueId());
            this.plugin.getSectorClient().sendGlobalPacket(packet);
            return;
        }
        if (e.getItem().getType().equals(Material.GOLDEN_APPLE) && e.getItem().getDurability() == 1) {
            final Packet packet = new UserEatKoxPacket(e.getPlayer().getUniqueId());
            this.plugin.getSectorClient().sendGlobalPacket(packet);
        }
    }

    private void addPotionEffects(List<PotionEffect> potionEffects, PlayerItemConsumeEvent event){
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,() -> {
            final Player player = event.getPlayer();
            for(PotionEffect potionEffect : potionEffects){
                if(player.hasPotionEffect(potionEffect.getType())){
                    player.removePotionEffect(potionEffect.getType());
                }
                player.addPotionEffect(potionEffect);
            }
        },1L);
    }
}
