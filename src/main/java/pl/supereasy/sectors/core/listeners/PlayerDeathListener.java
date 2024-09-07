package pl.supereasy.sectors.core.listeners;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.chat.SpecifiedMessageTypePacket;
import pl.supereasy.sectors.api.packets.impl.guild.WarUpdateValuesPacket;
import pl.supereasy.sectors.api.packets.impl.synchro.UserChangeSectorPacket;
import pl.supereasy.sectors.api.packets.impl.user.UserKillPacket;
import pl.supereasy.sectors.api.packets.impl.user.UserPointsUpdatePacket;
import pl.supereasy.sectors.api.packets.impl.user.UserSpawnTeleport;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.api.sectors.type.SectorType;
import pl.supereasy.sectors.core.combat.data.Combat;
import pl.supereasy.sectors.core.combat.util.DeathUtil;
import pl.supereasy.sectors.core.user.enums.MessageType;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.wars.api.War;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemUtil;
import pl.supereasy.sectors.util.TeleportUtil;
import pl.supereasy.sectors.util.title.TitleUtil;

import java.util.Map;
import java.util.UUID;

public class PlayerDeathListener implements Listener {

    private final SectorPlugin plugin;

    public PlayerDeathListener(SectorPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        e.setDeathMessage(null);
        final Player p = e.getEntity();
        final Combat pC = plugin.getCombatManager().getCombat(p);
        Player k = p.getKiller();
        final User pUser = plugin.getUserManager().getUser(p.getUniqueId());
        if (k == null && pC != null && pC.wasFight()) {
            k = pC.getLastAttackPlayer();
        } else if (k != null) {
            k.setFireTicks(0);
            final User kUser = plugin.getUserManager().getUser(k.getDisplayName());
            if (kUser == null) {
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
                if (DeathUtil.isAssist(pC)) {
                    assistList = DeathUtil.getAssistList(pC, k);
                }
                final Packet packet = new UserKillPacket(k.getUniqueId(), plus, p.getUniqueId(), minus, DeathUtil.deathMessage(plus, minus, p, k), assistList);
                this.plugin.getSectorClient().sendGlobalPacket(packet);
                if (kUser.getGuild() != null && pUser.getGuild() != null) {
                    final War war = this.plugin.getWarManager().getWar(kUser.getGuild(), pUser.getGuild());
                    if (war != null) {
                        if (war.getFirstGuild().getTag().equalsIgnoreCase(kUser.getGuild().getTag())) {
                            war.getFirstGuild().addKills(1);
                            war.getSecondGuild().addDeaths(1);
                        } else {
                            war.getFirstGuild().addDeaths(1);
                            war.getSecondGuild().addKills(1);
                        }
                        war.insert(true);
                        final Packet alliancePacket = new WarUpdateValuesPacket(war.getFirstGuild().getTag(), war.getFirstGuild().getKills(), war.getFirstGuild().getDeaths(),
                                war.getSecondGuild().getTag(), war.getSecondGuild().getKills(), war.getSecondGuild().getDeaths());
                        this.plugin.getSectorClient().sendPacket(alliancePacket);

                    }
                }
                //TitleUtil.sendTitle(k, 10, 100, 5, ChatUtil.fixColor("&8(->> &cZABOJSTWO &8<<-)"), ChatUtil.fixColor("&8->> &7Zabiles gracza &6" + p.getDisplayName() + "&8 (&7+&a&n" + plus + "&8) &8<<-"));
            }
        }
        if (pUser == null) {
            p.kickPlayer("Zglos sie do administratora! Blad krytyczny");
            return;
        }
        p.getWorld().strikeLightningEffect(p.getLocation());
        DeathUtil.remove(pC);
        p.getLocation().getWorld().dropItemNaturally(p.getLocation(), ItemUtil.getPlayerHead(p.getDisplayName()));
        if (this.plugin.getSectorManager().getCurrentSector().getSectorType() == SectorType.SPAWN) {
            final ItemStack[] content = p.getInventory().getContents();
            final ItemStack[] armor = p.getInventory().getArmorContents();

            p.getInventory().clear();
            p.getInventory().setArmorContents(new ItemStack[]{null, null, null, null});

            if(k == null) {
                pUser.removePoints(5);
                Packet packet = new UserPointsUpdatePacket(pUser.getUUID(), pUser.getPoints());
                plugin.getSectorClient().sendGlobalPacket(packet);
                for (ItemStack item : content) {
                    if (item != null && !item.getType().equals(Material.AIR)) {
                        p.getLocation().getWorld().dropItemNaturally(p.getLocation(), item);
                    }
                }
                for (ItemStack item : armor) {
                    if (item != null && !item.getType().equals(Material.AIR)) {
                        p.getLocation().getWorld().dropItemNaturally(p.getLocation(), item);
                    }
                }
            }else {
                ItemUtil.giveItems(k,content);
                ItemUtil.giveItems(k,armor);
            }
            e.getDrops().clear();
            Packet packet = new UserSpawnTeleport(p.getName(), this.plugin.getSectorManager().getCurrentSector().getSectorName());
            this.plugin.getSectorClient().sendGlobalPacket(packet);
            final Location fixLocation = p.getLocation().getWorld().getSpawnLocation();
            p.teleport(fixLocation);
            ((CraftPlayer) p).getHandle().setLocation(fixLocation.getX(), fixLocation.getY(), fixLocation.getZ(), fixLocation.getYaw(), fixLocation.getPitch());
            p.setHealth(20.0);
            p.setFoodLevel(20);
            p.setFireTicks(0);
            p.setFallDistance(0);
            p.setExp(0);
            p.setTotalExperience(0);
            p.setLevel(0);
            for (PotionEffect potion : p.getActivePotionEffects()) {
                p.removePotionEffect(potion.getType());
            }
            pUser.saveUserToValues();
        } else {
            final Sector spawn = TeleportUtil.getRandomSpawn();
            final String spawnName = (spawn == null ? "spawn-1" : spawn.getSectorName());
            Packet packet = new UserSpawnTeleport(p.getName(), spawnName);
            this.plugin.getSectorClient().sendGlobalPacket(packet);
            p.setHealth(20.0);
            p.setFoodLevel(20);
            p.setFireTicks(0);
            p.setFallDistance(0);
            p.setExp(0);
            p.setTotalExperience(0);
            p.setLevel(0);
            for (PotionEffect potion : p.getActivePotionEffects()) {
                p.removePotionEffect(potion.getType());
            }
            p.getInventory().clear();
            p.getInventory().setArmorContents(new ItemStack[]{null, null, null, null});
            if(k != null){
                ItemUtil.giveItems(e.getDrops(),k);
                e.getDrops().clear();
            }
            pUser.saveUserToValues(Bukkit.getWorld("world").getSpawnLocation());
            packet = new UserChangeSectorPacket(pUser);
            this.plugin.getSectorClient().sendUser(packet, spawnName);
        }
        p.kickPlayer(ChatUtil.fixColor("&8->> &cNie zyjesz, wejdz ponownie na serwer za 5 sekund! &8<<-"));
    }
}
