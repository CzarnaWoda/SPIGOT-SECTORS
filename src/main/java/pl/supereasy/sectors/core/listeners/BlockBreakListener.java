package pl.supereasy.sectors.core.listeners;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.build.enums.CanBuild;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.guild.GuildBreakHeartPacket;
import pl.supereasy.sectors.api.packets.impl.guild.GuildDestroyPacket;
import pl.supereasy.sectors.api.packets.impl.guild.GuildTakeLivePacket;
import pl.supereasy.sectors.api.sectors.type.SectorType;
import pl.supereasy.sectors.core.craftings.enums.CraftItem;
import pl.supereasy.sectors.core.generators.api.BlockGenerator;
import pl.supereasy.sectors.core.specialitems.types.SpecialItemType;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.alliances.api.GuildAlliance;
import pl.supereasy.sectors.guilds.alliances.impl.AllianceImpl;
import pl.supereasy.sectors.guilds.api.GuildMember;
import pl.supereasy.sectors.guilds.enums.AlliancePermission;
import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.guilds.enums.PermissionCategory;
import pl.supereasy.sectors.guilds.log.enums.GuildLogType;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemUtil;
import pl.supereasy.sectors.util.RandomUtil;
import pl.supereasy.sectors.util.Time;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class BlockBreakListener implements Listener {

    private final SectorPlugin plugin;
    private final Map<Material, GuildPermission> breakPerms = new HashMap<Material, GuildPermission>() {{
        Stream.of(GuildPermission.values())
                .filter(guildPermission -> guildPermission.getPermissionCategory() == PermissionCategory.BREAK)
                .forEach((guildPermission -> {
                    put(guildPermission.getMaterial(), guildPermission);
                }));
    }};

    public BlockBreakListener(SectorPlugin plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onBreakInGuild(BlockBreakEvent e) {
        if(e.isCancelled()){
            return;
        }
        final Player player = e.getPlayer();
        final Block block = e.getBlock();
        final User user = this.plugin.getUserManager().getUser(player.getUniqueId());
        final Guild guild = this.plugin.getGuildManager().getGuild(block.getLocation());
        final CanBuild canBuild = this.plugin.getBuildManager().isAllowBuild(player, user, block, guild);
        if (canBuild != CanBuild.ALLOW) {
            ChatUtil.sendMessage(player, canBuild.getMessage());
            e.setCancelled(true);
            return;
        }
        final BlockGenerator generator = this.plugin.getGeneratorManager().getGenerator(block.getLocation());

        if (generator != null) {
            if (guild != null) {
                if (!plugin.getUserManager().getUser(player.getUniqueId()).getGroup().equals(UserGroup.ADMIN) && user.getGuild() != null && user.getGuild().equals(guild)) {
                    final GuildMember guildMember = user.getGuild().getMember(user.getUUID());
                    if (!guildMember.hasPermission(GuildPermission.GENERATOR_BREAK)) {
                        ChatUtil.sendMessage(player, "&4Blad: &cNie posiadasz uprawnienia do niszczenia blokow nad generatorami");
                        e.setCancelled(true);
                        return;
                    }
                }
            }
            if (player.getItemInHand() != null && player.getItemInHand().getType() == Material.GOLD_PICKAXE) {
                if(!plugin.getSectorManager().getCurrentSector().getSectorType().equals(SectorType.SPAWN)) {
                    giveGenerator(player, block);
                    return;
                }else{
                    if(user.getGroup().getGroupLevel() >= UserGroup.HELPER.getGroupLevel()) {
                        giveGenerator(player, block);
                        return;
                    }
                }
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    e.getBlock().setType(generator.getMaterial());
                }
            }.runTaskLater(plugin, generator.regenerationDelay());
        }

        if (guild != null) {
            if (!guild.isMember(player.getUniqueId())) {
                if (user.getGuild() != null) {
                    if (guild.getGuildRegion().isCenter(e.getBlock().getLocation())) {
                        e.setCancelled(true);
                        if (e.getBlock().getType() == Material.SEA_LANTERN) {
                            e.setCancelled(true);
                            if (guild.isMember(e.getPlayer().getUniqueId())) {
                                guild.sendInfoMessage(e.getPlayer());
                                return;
                            }
                            if (guild.getGuildLastTakeLive() + Time.HOUR.getTime(24) > System.currentTimeMillis()) {
                                ChatUtil.sendMessage(e.getPlayer(), " &4Blad: &cTa gildia posiada aktualnie ochrone!");
                                return;
                            }
                            final Guild attackerGuild = this.plugin.getGuildManager().getGuild(e.getPlayer().getUniqueId());
                            if (attackerGuild == null || this.plugin.getAllianceManager().hasAlliance(attackerGuild, guild) || attackerGuild.equals(guild)) {
                                guild.sendInfoMessage(e.getPlayer());
                                return;
                            }
                            if (guild.getHeartDurability() <= 1) {
                                if (guild.getGuildLives() <= 0) {
                                    guild.delete(true);
                                    final Packet packet = new GuildDestroyPacket(guild.getTag(), attackerGuild.getTag());
                                    this.plugin.getSectorClient().sendGlobalPacket(packet);
                                    return;
                                } else {
                                    guild.setHeartDurability(500);
                                    guild.setGuildLastBreakHeart(System.currentTimeMillis());
                                    guild.insert(true);
                                    final Packet packet = new GuildTakeLivePacket(guild.getTag(), attackerGuild.getTag());
                                    this.plugin.getSectorClient().sendGlobalPacket(packet);
                                    //guild.sendInfoMessage(e.getPlayer());
                                    return;
                                }
                            } else {
                                final Packet packet = new GuildBreakHeartPacket(guild.getTag());
                                this.plugin.getSectorClient().sendGlobalPacket(packet);
                                return;
                                //TODO hologram update or smth
                            }

                        } else {
                            ChatUtil.sendMessage(e.getPlayer(), " &8» &cNie mozesz niszczyc blokow w centrum gildii!");
                            return;
                        }
                    }

                }
                //ChatUtil.sendMessage(player, "&4Blad: &cNie mozesz tutaj tego zrobic! To nie Twoja gildia");
                e.setCancelled(true);
                return;
            }
            final GuildMember guildMember = guild.getMember(e.getPlayer().getUniqueId());
            final boolean isOwner = guild.isOwner(guildMember.getUUID());
            if (!isOwner) {
                if (!guildMember.hasPermission(GuildPermission.BREAK)) {
                    player.sendMessage(ChatUtil.fixColor(" &8» &CNie mozesz tutaj niszczyc blokow! &8(&7Popros lidera o uprawnienie&8)"));
                    e.setCancelled(true);
                    return;
                }
                if (block.getType() == Material.BEACON && !guildMember.hasPermission(GuildPermission.BEACON_BREAK)) {
                    player.sendMessage(ChatUtil.fixColor(" &8» &CNie mozesz zniszczyc &bbeacona&7! &8(&7Popros lidera o uprawnienie&8)"));
                    e.setCancelled(true);
                    return;
                }
                final GuildPermission permission = this.breakPerms.get(e.getBlock().getType());
                if (permission != null && !guildMember.hasPermission(permission)) {
                    player.sendMessage(ChatUtil.fixColor(" &8» &cNie mozesz tutaj niszczyc tych blokow! &8(&7Popros lidera o uprawnienie&8)"));
                    e.setCancelled(true);
                    return;
                }
            }
            this.plugin.getGuildLogger().getLogger(GuildLogType.BREAK).update(user, block); //Guild Logblock
        }
        if (!player.getGameMode().equals(GameMode.CREATIVE)) {
            player.giveExp(plugin.getDropManager().getExp(block.getType()));
            plugin.getDropManager().getDropData(block.getType()).breakBlock(block, player, player.getItemInHand());
            player.spigot().playEffect(block.getLocation(), Effect.TILE_DUST, 1, 5, 0.2f, 0.3f, 0.2f, 0.15f, 8, 1925);
            //* sounds *//

            if (block.getType().equals(Material.STONE)) {
                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 0.5f, (float) (Math.random() * 20.0) / 10.0f);
                user.addMinedStone(1);
            } else if (block.getType().equals(Material.OBSIDIAN)) {
                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 0.5f, (float) (Math.random() * 20.0) / 10.0f);
            }
            e.setCancelled(true);
        }

    }

    private void giveGenerator(Player player, Block block) {
        if (player.getGameMode().equals(GameMode.SURVIVAL)) {
            final ItemStack generator1 = this.plugin.getCraftingManager().getRecipe(CraftItem.GENERATOR).getResult();
            generator1.setAmount(1);
            ItemUtil.giveItems(Collections.singletonList(generator1), player);
        }
        ChatUtil.sendMessage(player, "&6&lGENERATOR &8->> &7Zniczyles &6&nGENERATOR");
        this.plugin.getGeneratorManager().removeGenerator(block.getLocation());
    }
}
