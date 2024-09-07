package pl.supereasy.sectors.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.util.*;

import java.util.Random;

public class BossDamageByPlayerListener implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e){
        if (e.isCancelled()) return;

        if (e.getEntity() instanceof PigZombie) {

            if(DamageUtil.getDamager(e) != null){
                final Player player = DamageUtil.getDamager(e);

                player.spigot().playEffect(e.getEntity().getLocation().add(0.0, 1.0, 0.0), Effect.COLOURED_DUST, 8, 0, 0.8f, 0.8f, 0.8f, 0.04f, 50, 50);
                ActionBarUtil.sendActionBar(player, ChatUtil.fixColor("&8->> ( &d&lBOSS &8* &4&n" + (MathUtil.round(((PigZombie) e.getEntity()).getHealth(),1) + "&7/&c&n" + ((PigZombie) e.getEntity()).getMaxHealth() + "&8 ) <<-")));


                if(RandomUtil.getChance(0.2)){
                    final Location loc = player.getLocation();
                    loc.getWorld().strikeLightning(player.getLocation());
                    loc.getWorld().strikeLightning(player.getLocation());
                    loc.getWorld().strikeLightning(player.getLocation());
                    loc.getWorld().strikeLightning(player.getLocation());
                    ChatUtil.sendMessage(player, "&d&lBOSS &8->> &7Wyslalem na ciebie gniew &6&lZEUSA");
                }
                if(RandomUtil.getChance(0.34)){
                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,500,4));
                    ChatUtil.sendMessage(player, "&d&lBOSS &8->> &3&lPOSEJDON &7czasami zakręci w głowie");
                }
                if(RandomUtil.getChance(0.1)){
                    final Location loc = player.getLocation();
                    loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 20.0f, false, false);
                    ChatUtil.sendMessage(player, "&d&lBOSS &8->> &4&lWYBUCH!&7, jak teraz sie trzymasz?");
                }
                if(RandomUtil.getChance(0.4)){
                    ((PigZombie) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,60,3));
                    ChatUtil.sendMessage(player, "&d&lBOSS &8->> &d&lWIEDZMY!&7, ukryjcie mnie!");
                }
                if(RandomUtil.getChance(0.2)){
                    player.setVelocity(player.getLocation().getDirection().multiply(-10));
                    ChatUtil.sendMessage(player, "&d&lBOSS &8->> &c&lODEJDZ&7, nie wracaj!");
                    player.setFoodLevel(0);
                }
                if(RandomUtil.getChance(0.5)){
                    player.setVelocity(player.getLocation().getDirection().multiply(12));
                    ChatUtil.sendMessage(player, "&d&lBOSS &8->> &c&lZOBACZ&7, jak wysoko jestes!");
                }
                if(RandomUtil.getChance(0.3)){
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,950,4));
                    ChatUtil.sendMessage(player, "&d&lBOSS &8->> &6&lSLABO&7, wygladasz!");
                }
                if(RandomUtil.getChance(0.3)){
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,950,4));
                    ChatUtil.sendMessage(player, "&d&lBOSS &8->> &6&lWIDZISZ?&7, nie widzisz!");
                }
                if(RandomUtil.getChance(0.05)){
                    player.setVelocity(player.getLocation().getDirection().multiply(-5));
                    final Location loc = e.getEntity().getLocation();
                    loc.getWorld().strikeLightning(e.getEntity().getLocation());
                    for(int i = 0 ; i < 3 ; i ++) {
                        Zombie boss = (Zombie) Bukkit.getWorld("world").spawnEntity(loc, EntityType.ZOMBIE);
                        boss.setCustomName(ChatUtil.fixColor("&8->> ( &dSLUGA BOSSA &8) <<-"));
                        boss.setBaby(true);
                        boss.setMaxHealth(40.0D);
                        boss.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
                        boss.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
                        boss.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
                        boss.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
                        boss.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 4));
                        boss.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 1));
                    }
                    ChatUtil.sendMessage(player, "&d&lBOSS &8->> &a&lPOMOCY?&7, pomocnicy zabic go!");

                }
            }
        }
    }
}
