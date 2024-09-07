package pl.supereasy.sectors.core.listeners.explosion;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.util.*;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExplodeListener implements Listener {

    private final SectorPlugin plugin;
    private final int explodeRadius;
    private final Map<Material, Double> dropMap;

    public ExplodeListener(SectorPlugin plugin) {
        this.plugin = plugin;
        float unalteredRadius = (float) 2.0;
        this.explodeRadius = (int) Math.ceil(unalteredRadius);
        this.dropMap = new HashMap<>();
        this.dropMap.put(Material.OBSIDIAN, (double) GuildConfig.INSTANCE.VALUES_GUILD_TNT_OBSIDIANDESTROY);
    }

    @EventHandler(priority =  EventPriority.MONITOR)
    public void onExplode(final EntityExplodeEvent e) {
        if (e.isCancelled()) return;
        if(!plugin.getTntManager().isTntEnable()){
            e.setCancelled(true);
            return;
        }
        final Location detonatorLoc = e.getLocation();
        Guild guild = this.plugin.getGuildManager().getGuild(detonatorLoc);
        if (guild == null) {
            for (final Block b : e.blockList()) {
                if (guild != null) {
                    continue;
                }
                final Guild o = plugin.getGuildManager().getGuild(b.getLocation());
                if (o == null) {
                    continue;
                }
                guild = o;
            }
        }
        if (guild == null) {
            e.setCancelled(true);
            return;
        }
        /*for (int x = -explodeRadius; x <= explodeRadius; x++) {
            for (int y = -explodeRadius; y <= explodeRadius; y++) {
                for (int z = -explodeRadius; z <= explodeRadius; z++) {
                    final Location targetLoc = new Location(detonatorLoc.getWorld(), detonatorLoc.getX() + x, detonatorLoc.getY() + y, detonatorLoc.getZ() + z);
                    Material m = targetLoc.getBlock().getType();
                    final Block b = targetLoc.getBlock();
                    if (b.getType() == Material.AIR || b.getType() == Material.TNT) {
                        continue;
                    }
                    if (!guild.getGuildRegion().isCenter(b.getLocation())) {
                        guild.getRegenerationBlocks().add(new RegenerationBlock(guild, b.getLocation(), b.getType()));
                        Location finalTargetLoc = targetLoc;
                        Option.of(dropMap.get(m))
                                .onEmpty(() -> {
                                    if (m != Material.TNT && m != Material.BEDROCK && m != Material.ANVIL && m != Material.ENCHANTMENT_TABLE) {
                                        finalTargetLoc.getBlock().breakNaturally();
                                        if (b.getType() != Material.AIR) {
                                            this.plugin.getGuildRegeneration().addBlock(guild, b);
                                            guild.getRegenerationBlocks().add(new RegenerationBlock(guild, b.getLocation(), b.getType()));
                                            Bukkit.broadcastMessage("Explode debug 1");
                                        }
                                    }
                                })
                                .filter(aDouble -> {
                                    if (m == Material.OBSIDIAN) {
                                        final GuildUpgrade guildUpgrade = guild.getUpgrade(UpgradeType.OBSIDIAN);
                                        double chance = Math.random() * 100;
                                        if (guildUpgrade != null) {
                                            chance = chance - (int) guildUpgrade.getValue();
                                        }
                                        return chance <= aDouble;
                                    }
                                    return true;
                                })
                                .peek(aDouble -> {
                                    finalTargetLoc.getBlock().breakNaturally();
                                    if (b.getType() != Material.AIR || b.getType() != Material.TNT) {
                                        this.plugin.getGuildRegeneration().addBlock(guild, b);
                                        guild.getRegenerationBlocks().add(new RegenerationBlock(guild, b.getLocation(), b.getType()));
                                        Bukkit.broadcastMessage("Explode debug 2");
                                    }
                                });
                    }
                }
            }
        }*/
        if (guild.getGuildRegion().isCenter(e.getLocation())) {
            e.setCancelled(true);
            return;
        }
        if (guild.getGuildCreateTime() + Time.HOUR.getTime(24)  > System.currentTimeMillis()) {
            e.setCancelled(true);
            return;
        }
        final Calendar c = Calendar.getInstance();
        if (c.get(Calendar.HOUR_OF_DAY) > 22 && c.get(Calendar.HOUR_OF_DAY) < 10) {
            e.setCancelled(true);
            return;
        }
        for(Block b : e.blockList()){
            this.plugin.getGuildRegeneration().addBlock(guild, b);
        }
        final List<Location> sphere = SpaceUtil.sphere(e.getLocation(), 5, 5, false, true, 0);
        for (final Location location : sphere) {
            if (location.getBlock().getType() == Material.OBSIDIAN) {
                double chance = Math.random() * 100;
                if(guild.getGuildRegion().isCenter(location)){
                    continue;
                }
                if (!RandomUtil.getChance(5.0)) {
                    continue;
                }
                this.plugin.getGuildRegeneration().addBlock(guild, location.getBlock());
                location.getBlock().setType(Material.AIR);
            }
            else if (location.getBlock().getType() == Material.STATIONARY_WATER) {
                if (!RandomUtil.getChance(10.0)) {
                    continue;
                }
                this.plugin.getGuildRegeneration().addBlock(guild, location.getBlock());
                location.getBlock().setType(Material.AIR);
            }
            else if (location.getBlock().getType() == Material.STATIONARY_LAVA) {
                if (!RandomUtil.getChance(10.0)) {
                    continue;
                }
                this.plugin.getGuildRegeneration().addBlock(guild, location.getBlock());
                location.getBlock().setType(Material.AIR);
            }
            else if (location.getBlock().getType() == Material.WATER) {
                if (!RandomUtil.getChance(50.0)) {
                    continue;
                }
                this.plugin.getGuildRegeneration().addBlock(guild, location.getBlock());
                location.getBlock().setType(Material.AIR);
            }
            else if (location.getBlock().getType() == Material.LAVA) {
                if (!RandomUtil.getChance(50.0)) {
                    continue;
                }
                this.plugin.getGuildRegeneration().addBlock(guild, location.getBlock());
                location.getBlock().setType(Material.AIR);
            }
            else if (location.getBlock().getType() == Material.ENDER_CHEST) {
                if (!RandomUtil.getChance(50.0)) {
                    continue;
                }
                this.plugin.getGuildRegeneration().addBlock(guild, location.getBlock());
                location.getBlock().setType(Material.AIR);
            }
            else {
                if (location.getBlock().getType() != Material.ENCHANTMENT_TABLE || !RandomUtil.getChance(50.0)) {
                    continue;
                }
                this.plugin.getGuildRegeneration().addBlock(guild, location.getBlock());
                location.getBlock().setType(Material.AIR);
            }
        }

        //Memento snapshot
        if (guild.getGuildLastExplosion() == -1L || guild.getGuildLastExplosion() + Time.MINUTE.getTime(2) < System.currentTimeMillis()) {
            guild.setGuildLastExplosion(System.currentTimeMillis());
            for (Player onlineMember : guild.getOnlineMembersAsPlayers()) {//TODO global packet
                ChatUtil.sendMessage(onlineMember, " &8» &cNa terenie twojej gildii wybuchlo TNT!");
            }
        }


    }

}
