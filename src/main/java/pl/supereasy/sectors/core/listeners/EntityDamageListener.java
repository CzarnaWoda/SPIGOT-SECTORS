package pl.supereasy.sectors.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.user.UserKillPacket;
import pl.supereasy.sectors.core.combat.api.CombatManager;
import pl.supereasy.sectors.core.combat.data.Combat;
import pl.supereasy.sectors.core.combat.util.DeathUtil;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EntityDamageListener implements Listener {

    private final SectorPlugin plugin;
    private final List<Material> swords;

    public EntityDamageListener(SectorPlugin plugin) {
        this.plugin = plugin;
        this.swords = Arrays.asList(Material.DIAMOND_SWORD,Material.IRON_SWORD,Material.GOLD_SWORD,Material.WOOD_SWORD,Material.STONE_SWORD);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            final Player p = (Player) e.getEntity();
            if(swords.contains(p.getItemInHand().getType())){
                if(p.getItemInHand().getItemMeta().hasEnchant(Enchantment.KNOCKBACK)){
                    if(!plugin.getCoreConfig().TOGGLEMANAGER_KNOCKBACK){
                        p.getItemInHand().removeEnchantment(Enchantment.KNOCKBACK);
                        e.setCancelled(true);
                        return;
                    }
                }
            }
            /*if (p.getHealth() - e.getFinalDamage() <= 0.0D) {
                e.setCancelled(true);
                p.setHealth(20.0D);
                p.setFoodLevel(20);
                p.setSaturation(20.0F);
                final ItemStack[] content = p.getInventory().getContents().clone();
                final ItemStack[] armor = p.getInventory().getArmorContents().clone();
                final Location loc = p.getLocation();
                p.getInventory().clear();
                for (ItemStack itemStack : content) {
                    if (itemStack != null && itemStack.getType() != Material.AIR)
                        loc.getWorld().dropItemNaturally(loc, itemStack.clone());
                }
                for (ItemStack itemStack : armor) {
                    if (itemStack != null && itemStack.getType() != Material.AIR)
                        loc.getWorld().dropItemNaturally(loc, itemStack.clone());
                }
                p.getInventory().setArmorContents(new ItemStack[p.getInventory().getArmorContents().length]);
                p.getInventory().clear();
                final User user = this.plugin.getUserManager().getUser(p.getUniqueId());
                this.plugin.getTeleportManager().teleportToSpawn(user, p, true);
                final Combat pC = plugin.getCombatManager().getCombat(p);
                Player k = p.getKiller();
                final User pUser = plugin.getUserManager().getUser(p.getUniqueId());
                if(k == null && pC != null && pC.wasFight()){
                    k = pC.getLastAttackPlayer();
                }else if(k != null){
                    final User kUser = plugin.getUserManager().getUser(k.getDisplayName());
                    if(kUser == null){
                        return;
                    }
                    if(p.equals(k)){
                        return;
                    }
                    if (DeathUtil.isLastKill(kUser, p)) {
                        ChatUtil.sendMessage(k, "&4Uwaga, &costatnio zabiles tego samego gracza, nie otrzymujesz punktow !");
                    } else {
                        final int plus = DeathUtil.calculateAddRanking(kUser, pUser);
                        final int minus = DeathUtil.calculateRemoveRanking(kUser, pUser);
                        Map<UUID, Integer> assistList = null;
                        System.out.println(pC.getDamage().toString());
                        System.out.println(pC.getAssists().toString());

                        if (DeathUtil.isAssist(pC)) {
                            assistList = DeathUtil.getAssistList(pC, k);
                            System.out.println("ASSISTLIST: " + assistList.size());
                            Map<UUID, Integer> finalAssistList = assistList;
                            assistList.keySet().forEach(uuid->System.out.println("UUID: " + uuid + ", procent: " + finalAssistList.get(uuid)));
                        }
                        final Packet packet = new UserKillPacket(k.getUniqueId(), plus, p.getUniqueId(), minus, DeathUtil.deathMessage(plus, minus, p, k), assistList);
                        this.plugin.getSectorClient().sendGlobalPacket(packet);
                        //TitleUtil.sendTitle(k, 10, 100, 5, ChatUtil.fixColor("&8(->> &cZABOJSTWO &8<<-)"), ChatUtil.fixColor("&8->> &7Zabiles gracza &6" + p.getDisplayName() + "&8 (&7+&a&n" + plus + "&8) &8<<-"));
                    }
                } else if (pUser != null) {
                    pUser.removePoints(5);
                }
                DeathUtil.remove(pC);
                loc.getWorld().dropItemNaturally(loc, ItemUtil.getPlayerHead(p.getDisplayName()));
                Bukkit.broadcastMessage("wyczyszczono eq");
            }*/
        }
    }
}
