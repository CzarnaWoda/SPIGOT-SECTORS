package pl.supereasy.sectors.core.listeners;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.build.enums.CanBuild;
import pl.supereasy.sectors.api.sectors.type.SectorType;
import pl.supereasy.sectors.core.autofarmer.api.AutomaticFarmer;
import pl.supereasy.sectors.core.craftings.api.CraftingRecipe;
import pl.supereasy.sectors.core.craftings.enums.CraftItem;
import pl.supereasy.sectors.core.generators.api.BlockGenerator;
import pl.supereasy.sectors.core.generators.impl.BlockGeneratorImpl;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.alliances.api.GuildAlliance;
import pl.supereasy.sectors.guilds.api.GuildMember;
import pl.supereasy.sectors.guilds.enums.AlliancePermission;
import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.guilds.enums.PermissionCategory;
import pl.supereasy.sectors.util.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class BlockPlaceListener implements Listener {

    private final SectorPlugin plugin;
    private final Map<Material, GuildPermission> placePerms = new HashMap<Material, GuildPermission>() {{
        Stream.of(GuildPermission.values())
                .filter(guildPermission -> guildPermission.getPermissionCategory() == PermissionCategory.PLACE)
                .forEach((guildPermission -> {
                    put(guildPermission.getMaterial(), guildPermission);
                }));
    }};
    private final Map<Material, GuildPermission> craftItemsPlace = new HashMap<Material, GuildPermission>() {{
        put(Material.STONE, GuildPermission.KOPACZFOSY_PLACE);
        put(Material.OBSIDIAN, GuildPermission.BOYFARMER_PLACE);
        put(Material.SAND, GuildPermission.SANDFARMER_PLACE);
    }};
    private final Cache<UUID, Long> times;

    public BlockPlaceListener(SectorPlugin plugin) {
        this.plugin = plugin;
        this.times = CacheBuilder.newBuilder().expireAfterWrite(3,TimeUnit.SECONDS).build();
        // materialList = Arrays.asList(Material.CHEST,Material.WORKBENCH,Material.FURNACE,Material.ENDER_CHEST,Material.ENDER_STONE);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (e.isCancelled()) return;
        final Guild g = this.plugin.getGuildManager().getGuild(e.getBlock().getLocation());
        final Player p = e.getPlayer();
        final User user = this.plugin.getUserManager().getUser(e.getPlayer().getUniqueId());
        final CanBuild canBuild = this.plugin.getBuildManager().isAllowBuild(p, user, e.getBlock(), g);
        if(g == null && canBuild.equals(CanBuild.ALLOW)) {
            if (this.plugin.getSectorManager().getCurrentSector().getSectorType() != SectorType.SPAWN) {
                if (e.getBlockPlaced().getType() != Material.CHEST && e.getBlockPlaced().getY() > 55) {
                    if (!e.getPlayer().isSneaking()) {
                        plugin.getAntiGriefManager().addBlockLocation(e.getBlock());
                        sendMessage(p, "&6ANTIGRIEF &8->> &7Postawiles blok ktory za &c5 minut &7usunie sie, jezeli chcesz aby byl on na stale poloz go z &cSHIFTEM");
                    } else {
                        sendMessage(p, "&6ANTIGRIEF &8->> &7Postawiony blok nie zaliczyl sie do &cusuniecia&7!");
                    }
                }
            }
        }
        if (canBuild != CanBuild.ALLOW) {
            ChatUtil.sendMessage(p, canBuild.getMessage());
            e.setCancelled(true);
            return;
        }
        if(g != null) {
            final Player player = e.getPlayer();
            if (!g.isMember(player.getUniqueId())) {
                //sendMessage(player, "&4Blad: &cNie mozesz tutaj tego zrobic! To nie Twoja gildia");
                e.setCancelled(true);
                return;
            }
            final GuildMember guildMember = g.getMember(e.getPlayer().getUniqueId());
            final boolean isOwner = g.isOwner(guildMember.getUUID());
            if (!isOwner) {
                if (!guildMember.hasPermission(GuildPermission.PLACE)) {
                    player.sendMessage(ChatUtil.fixColor(" &8» &cNie mozesz tutaj stawiac blokow! &8(&7Popros lidera o uprawnienie&8)"));
                    e.setCancelled(true);
                    return;
                }
                final GuildPermission permission = this.placePerms.get(e.getBlockPlaced().getType());
                if (permission != null && !guildMember.hasPermission(permission)) {
                    player.sendMessage(ChatUtil.fixColor(" &8» &cNie mozesz tutaj stawiac tych blokow! &8(&7Popros lidera o uprawnienie&8)"));
                    e.setCancelled(true);
                    return;
                }
            }
            if (g.getGuildRegion().isCenter(e.getBlock().getLocation())) {
                ChatUtil.sendMessage(player, " &8» &cNie mozesz klasc blokow w centrum gildii!");
                e.setCancelled(true);
                return;
            }
            if (g.isDuringExplosionAttack()) {
                final long time = g.getGuildLastExplosion() + Time.SECOND.getTime(30);
                final long timeSubtract = System.currentTimeMillis() - time;
                ChatUtil.sendMessage(p, " &4Blad: &cNa Twoim terenie ostatnio wybuchlo TNT. Nie mozesz stawiac blokow jeszcze przez " + Util.secondsToString((int) (timeSubtract / 1000L)));
                e.setCancelled(true);
                return;
            }
            if (e.getItemInHand() != null) {
                final ItemStack is = this.plugin.getCraftingManager().getRecipe(CraftItem.GENERATOR).getResult();
                if (ItemUtil.isEqual(is, p.getItemInHand())) {
                    BlockGenerator blockGenerator = this.plugin.getGeneratorManager().getGenerator(e.getBlock().getLocation());
                    if (blockGenerator != null) {
                        ChatUtil.sendMessage(p, "&6&lGENERATOR &8->> &cUwaga&7, w tym miejscu jest juz postawiony generator!");
                        return;
                    }
                    if (e.getBlockPlaced().getLocation().getY() >= 100) {
                        e.setCancelled(true);
                        ChatUtil.sendMessage(p, "&4Blad: &cNie mozesz stawiac zadnych generatorow powyzej y:100");
                        return;
                    }
                    blockGenerator = new BlockGeneratorImpl(is.getType(), 20);
                    this.plugin.getGeneratorManager().addGenerator(e.getBlock().getLocation(), blockGenerator);
                    ChatUtil.sendMessage(p, "&6&lGENERATOR &8->> &7Utworzono generator, ktory bedzie generowal &6&n" + ItemUtil.getPolishMaterial(e.getItemInHand().getType()));
                    return;
                }
                final AutomaticFarmer farmer = this.plugin.getAutomaticFarmerManager().getFarmer(e.getItemInHand());
                if (farmer != null) {
                    final GuildPermission craftPlacePermission = this.craftItemsPlace.get(e.getItemInHand().getType());
                    if (craftPlacePermission != null && !isOwner && !guildMember.hasPermission(craftPlacePermission)) {
                        player.sendMessage(ChatUtil.fixColor(" &8» &cNie mozesz postawic tego generatora! &8(&7Popros lidera o uprawnienie&8)"));
                        e.setCancelled(true);
                        return;
                    }
                    if (e.getBlockPlaced().getLocation().getY() >= 100) {
                        e.setCancelled(true);
                        ChatUtil.sendMessage(p, "&4Blad: &cNie mozesz stawiac zadnych generatorow powyzej y:100");
                        return;
                    }
                    farmer.run(e.getBlock().getLocation());
                }
            }
        } else {
            if (e.getItemInHand() != null) {
                final ItemStack is = this.plugin.getCraftingManager().getRecipe(CraftItem.GENERATOR).getResult();
                if (ItemUtil.isEqual(is, e.getItemInHand())) {
                    BlockGenerator blockGenerator = this.plugin.getGeneratorManager().getGenerator(e.getBlock().getLocation());
                    if (blockGenerator != null) {
                        ChatUtil.sendMessage(p, "&6&lGENERATOR &8->> &cUwaga&7, w tym miejscu jest juz postawiony generator!");
                        return;
                    }
                    blockGenerator = new BlockGeneratorImpl(is.getType(), 20);
                    this.plugin.getGeneratorManager().addGenerator(e.getBlock().getLocation(), blockGenerator);
                    ChatUtil.sendMessage(p, "&6&lGENERATOR &8->> &7Utworzono generator, ktory bedzie generowal &6&n" + ItemUtil.getPolishMaterial(e.getItemInHand().getType()));
                    return;
                }
                final AutomaticFarmer farmer = this.plugin.getAutomaticFarmerManager().getFarmer(e.getItemInHand());
                if (farmer != null) {
                    e.setCancelled(true);
                    ChatUtil.sendMessage(e.getPlayer(), "&4Blad: &cNie mozesz stawiac generatorow blokow poza terenem gildii!");
                }
            }
        }
    }
    private void sendMessage(Player player, String message){
        if(times.getIfPresent(player.getUniqueId()) == null){
            ChatUtil.sendMessage(player,message);
            times.put(player.getUniqueId(),System.currentTimeMillis());
        }
    }

}
