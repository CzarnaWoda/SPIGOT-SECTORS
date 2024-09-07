package pl.supereasy.sectors.core.listeners;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.combat.data.Combat;
import pl.supereasy.sectors.core.craftings.enums.CraftItem;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.DamageUtil;
import pl.supereasy.sectors.util.ItemUtil;
import pl.supereasy.sectors.util.Time;

import java.util.concurrent.TimeUnit;

public class EntityDamageByEntityListener implements Listener {

    private final SectorPlugin plugin;
    private final Cache<Player, Long> messages;

    public EntityDamageByEntityListener(SectorPlugin plugin) {
        this.plugin = plugin;
        messages = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.SECONDS).build();
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerDamageByPlayerHighest(final EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) {
            return;
        }
        final Player damaged = (Player) event.getEntity();
        if(!(event.getDamager() instanceof Player)) {
            return;
        }
        final Player damager = (Player) event.getDamager();
        if(damaged.isBlocking()){
            final ItemStack damagedItem = damaged.getItemInHand().clone();
            if(damagedItem != null && damagedItem.getType().toString().contains("SWORD")){
                damaged.setItemInHand(null);
                damaged.setItemInHand(damagedItem);
            }
        }
        if(damager.isBlocking()){
            final ItemStack damagerItem = damager.getItemInHand().clone();
            if(damagerItem != null && damagerItem.getType().toString().contains("SWORD")){
                damager.setItemInHand(null);
                damager.setItemInHand(damagerItem);
            }
        }
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if(!(e.getEntity() instanceof Player)){
            return;
        }
        if (e.getDamage() < 0.0D) {
            return;
        }
        final Player d = DamageUtil.getDamager(e);
        if(d == null){
            return;
        }
        final Player p = (Player)e.getEntity();
        final Combat c = plugin.getCombatManager().getCombat(p);
        if (c == null) {
            return;
        }
        if(p.equals(d)){
            c.setLastAttackTime(System.currentTimeMillis() + Time.SECOND.getTime(20));
            if (!c.hasFight()) {
                sendMessage(p, "&cANTYLOGOUT &8->> &4Jestes podczas walki, nie mozesz wylogowac sie! Walka trwa 20 sekund");
            }
            return;
        }
        final User user = plugin.getUserManager().getUser(p.getDisplayName());
        if (user.isProtection()) {
            e.setCancelled(true);
            sendMessage(d, "&4Blad: &cGracz posiada ochrone!");
            return;
        }
        final User userDamager = plugin.getUserManager().getUser(d.getDisplayName());
        if (userDamager.isProtection()) {
            e.setCancelled(true);
            sendMessage(d, "&4Blad: &cPosiadasz ochrone, nie mozesz sie bic!");
            return;
        }
        if (user.getGuild() != null && userDamager.getGuild() != null) {
            if (plugin.getAllianceManager().hasAlliance(user.getGuild(), userDamager.getGuild())) {
                if (!userDamager.getGuild().isAllianceFriendlyFire()) {
                    e.setCancelled(true);
                    e.setDamage(0);
                    return;
                }
            }
            if (user.getGuild().equals(userDamager.getGuild())) {
                e.setDamage(0);
                if(!user.getGuild().isFriendlyFire()){
                    e.setCancelled(true);
                }
                return;
            }
        }
        if(user.getGroup().hasPermission(UserGroup.HELPER)){
            return;
        }
        if (!c.hasFight()) {
            sendMessage(p, "&cANTYLOGOUT &8->> &4Jestes podczas walki, nie mozesz wylogowac sie! Walka trwa 20 sekund");
        }
        final Combat dc = plugin.getCombatManager().getCombat(d);
        if(dc != null){
            if(!dc.hasFight()){
                sendMessage(d, "&cANTYLOGOUT &8->> &4Jestes podczas walki, nie mozesz wylogowac sie! Walka trwa 20 sekund");
            }
            dc.setLastAttackTime(System.currentTimeMillis() + Time.SECOND.getTime(20));
            dc.setLastAttackPlayer(p);
        }
        c.setLastAttackTime(System.currentTimeMillis() + Time.SECOND.getTime(20));
        if (c.getLastAttackPlayer() != d) {
            c.getAssists().put(d, System.currentTimeMillis() + Time.SECOND.getTime(20));
        }
        c.setLastAttackPlayer(d);
        if (c.getDamage().get(d) != null) {
            int damage = c.getDamage().get(d);
            c.getDamage().put(d, (int) (damage + e.getFinalDamage()));
        } else {
            c.getDamage().put(d, (int) e.getFinalDamage());
        }

        final ItemStack itemStack = this.plugin.getCraftingManager().getRecipe(CraftItem.ANTY_NOGI).getResult();
        if (p.getItemInHand() != null && itemStack != null) {
            if (ItemUtil.isEqual(p.getItemInHand(), itemStack)) {
                p.teleport(d);
                p.setItemInHand(new ItemStack(Material.AIR));
            }
            return;
        }

        if (d.equals(p)) {
            if (!plugin.getCoreConfig().TOGGLEMANAGER_PUNCH) {
                ChatUtil.sendMessage(p, "&4Blad: &cPunch jest zablokowany na serwerze !");
                e.setCancelled(true);
            }
        }
    }
    private void sendMessage(Player player , String message){
        if(messages.getIfPresent(player) == null){
            ChatUtil.sendMessage(player, message);
            messages.put(player,System.currentTimeMillis());
        }
    }
}
