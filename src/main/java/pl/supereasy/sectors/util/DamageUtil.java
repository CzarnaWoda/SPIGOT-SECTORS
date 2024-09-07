package pl.supereasy.sectors.util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageUtil {

    public static Player getDamager(EntityDamageByEntityEvent e) {
        Entity damager = e.getDamager();
        if (damager instanceof Player) {
            return (Player)damager;
        }
        if (damager instanceof Projectile) {
            Projectile p = (Projectile)damager;
            if (p.getShooter() instanceof Player) {
                return (Player)p.getShooter();
            }
        }
        return null;
    }
}
