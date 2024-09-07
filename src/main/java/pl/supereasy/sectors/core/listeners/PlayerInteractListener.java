package pl.supereasy.sectors.core.listeners;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Button;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.guild.GuildDestroyPacket;
import pl.supereasy.sectors.api.packets.impl.guild.GuildTakeLivePacket;
import pl.supereasy.sectors.api.sectors.type.SectorType;
import pl.supereasy.sectors.config.CoreConfig;
import pl.supereasy.sectors.core.cases.api.Case;
import pl.supereasy.sectors.core.combat.data.Combat;
import pl.supereasy.sectors.core.combat.util.DeathUtil;
import pl.supereasy.sectors.core.specialitems.api.SpecialItem;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.core.user.inventory.EnderchestInventory;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.api.GuildMember;
import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.guilds.log.enums.GuildLogType;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.LocationUtil;
import pl.supereasy.sectors.util.Time;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class PlayerInteractListener implements Listener {

    private final SectorPlugin plugin;
    private final HashMap<UUID, Long> times;
    private final HashMap<UUID, Long> messageTimes;
    private final List<Material> swords;
    private final Cache<UUID, Long> clickDelay;

    public PlayerInteractListener(SectorPlugin plugin) {
        this.plugin = plugin;
        this.times = new HashMap<>();
        this.messageTimes = new HashMap<>();
        this.swords = Arrays.asList(Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLD_SWORD, Material.WOOD_SWORD, Material.STONE_SWORD);
        this.clickDelay = CacheBuilder.newBuilder().expireAfterWrite(3, TimeUnit.SECONDS).build();
    }


    @EventHandler
    public void onSoloRandomTeleport(PlayerInteractEvent event) {
        if (!event.hasBlock()) return;
        if (this.plugin.getSectorManager().getCurrentSector().getSectorType() != SectorType.SPAWN) return;
        Block clicked = event.getClickedBlock();
        if (clicked.getType() != Material.STONE_BUTTON) return;
        Button button = (Button) clicked.getState().getData();
        Block face = clicked.getRelative(button.getAttachedFace());
        if (face.getType() != Material.SPONGE) return;
        if (this.plugin.getTeleportManager().hasTeleportRequest(event.getPlayer().getUniqueId())) {
            sendMessage(event.getPlayer(), " &8» &cNie mozesz klikac w przycisk podczas teleportacji!");
            event.setCancelled(true);
            return;
        }
        if (clickDelay.getIfPresent(event.getPlayer().getUniqueId()) != null) {
            sendMessage(event.getPlayer(), "&cNie mozesz klikac w przycisk tak szybko!");
            event.setCancelled(true);
            return;
        }
        event.setCancelled(true);
        final User user = this.plugin.getUserManager().getUser(event.getPlayer().getUniqueId());
        this.plugin.getTeleportManager().teleportToRandomSector(user, event.getPlayer());
    }

    @EventHandler
    public void onGroupRandomTeleport(PlayerInteractEvent e){
        if (!e.hasBlock()) return;
        if (this.plugin.getSectorManager().getCurrentSector().getSectorType() != SectorType.SPAWN) return;
        Block clicked = e.getClickedBlock();
        if (clicked.getType() != Material.WOOD_BUTTON) return;
        if(plugin.getCoreConfig().RANDOMTELEPORT_1VS1LOCATIONS.contains(clicked.getLocation())) {
            if (this.plugin.getTeleportManager().hasTeleportRequest(e.getPlayer().getUniqueId())) {
                sendMessage(e.getPlayer(), " &8» &cNie mozesz klikac w przycisk podczas teleportacji!");
                e.setCancelled(true);
                return;
            }
            if (clickDelay.getIfPresent(e.getPlayer().getUniqueId()) != null) {
                sendMessage(e.getPlayer(), "&cNie mozesz klikac w przycisk tak szybko!");
                e.setCancelled(true);
                return;
            }
            final Button button = (Button) clicked.getState().getData();
            final Block base = clicked.getRelative(button.getAttachedFace());
            final List<Player> players = LocationUtil.getPlayersInRadius(base.getLocation(), 6);

            if (players.size() < 2) {
                return;
            }
            final List<User> users = new ArrayList<>();
            for (Player player : players) {
                users.add(plugin.getUserManager().getUser(player.getUniqueId()));
            }
            users.sort((g0, g1) -> {
                final Integer user1 = g0.getPoints();
                final Integer user2 = g1.getPoints();
                return user2.compareTo(user1);
            });
            int  index  = 0;
            while (Math.abs(users.get(index).getPoints() - users.get(index+1).getPoints()) >= 500){
                index ++;
                if(index + 1 >= users.size()){
                    return;
                }
            }
            if(Math.abs(users.get(index).getPoints() - users.get(index+1).getPoints()) >= 500){
                for(Player player : players){
                    sendMessage(player, "&4Blad: &cNie udało się znaleść gracza, którego różnica punktów względem twoich jest mniejsza niż 500");
                    return;
                }
            }else{

                plugin.getTeleportManager().teleportPlayersToRandomSector(Arrays.asList(users.get(index), users.get(index + 1)));
            }
        }
        if(plugin.getCoreConfig().RANDOMTELEPORT_XVSXLOCATIONS.contains(clicked.getLocation())) {
            if (this.plugin.getTeleportManager().hasTeleportRequest(e.getPlayer().getUniqueId())) {
                sendMessage(e.getPlayer(), " &8» &cNie mozesz klikac w przycisk podczas teleportacji!");
                e.setCancelled(true);
                return;
            }
            if (clickDelay.getIfPresent(e.getPlayer().getUniqueId()) != null) {
                sendMessage(e.getPlayer(), "&cNie mozesz klikac w przycisk tak szybko!");
                e.setCancelled(true);
                return;
            }
            final Button button = (Button) clicked.getState().getData();
            final Block base = clicked.getRelative(button.getAttachedFace());
            final List<Player> players = LocationUtil.getPlayersInRadius(base.getLocation(), 6);

            if (players.size() < 2) {
                return;
            }
            while (players.size() % 2 != 0) {
                if (players.size() < 2) {
                    return;
                }
                players.remove(players.size()-1);
            }
            final List<User> users = new ArrayList<>();
            for(Player player : players){
                users.add(plugin.getUserManager().getUser(player.getUniqueId()));
            }
            if(users.size() >= 2){
                plugin.getTeleportManager().teleportPlayersToRandomSector(users);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.isCancelled()) return;
        if (this.plugin.getSectorManager().getCurrentSector().howCloseBorderInSector(e.getPlayer().getLocation()) > 30.0) {
            for (Case c : plugin.getCaseManager().getCases().values()) {
                c.openAction(e, e.getPlayer());
            }
        }
        final User u = this.plugin.getUserManager().getUser(e.getPlayer().getUniqueId());
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (this.plugin.getTeleportManager().hasTeleportRequest(e.getPlayer().getUniqueId())) {
                sendMessage(e.getPlayer(), " &8» &cNie mozesz otwierac enderchesta podczas teleportacji!");
                e.setCancelled(true);
                return;
            }
            if (e.getClickedBlock().getType().equals(Material.ENDER_CHEST)) {
                final Combat c = plugin.getCombatManager().getCombat(e.getPlayer());
                if (c != null && c.hasFight()) {
                    e.setCancelled(true);
                    sendMessage(e.getPlayer(), "&4Blad: &cNie mozesz otwierac enderchesta podczas &6walki&c!");
                    return;
                }
                e.setCancelled(true);
                EnderchestInventory.openInventory(e.getPlayer(), u);
                return;
            }
            if(e.getClickedBlock().getType().equals(Material.ENCHANTMENT_TABLE)){
                if(!plugin.getCoreConfig().isEnchant()){
                    e.setCancelled(true);
                }
            }
            if(e.getClickedBlock().getType().equals(Material.WORKBENCH)) {
                final Combat c = plugin.getCombatManager().getCombat(e.getPlayer());
                if (c != null && c.hasFight()) {
                    e.setCancelled(true);
                    sendMessage(e.getPlayer(), "&4Blad: &cNie mozesz otwierac workbencha podczas &6walki&c!");
                    return;
                }
            }
        }
        if (e.getClickedBlock() == null) return;
        final Guild guild = this.plugin.getGuildManager().getGuild(e.getClickedBlock().getLocation());
        if (guild != null) {
            GuildMember guildMember = guild.getMember(e.getPlayer().getUniqueId());
            if (e.getClickedBlock().getType() == Material.CHEST && guild.isMember(guildMember.getUUID())) {
                if (!guildMember.hasPermission(GuildPermission.CHEST_OPEN)) {
                    sendMessage(e.getPlayer(), " &8» &cNie posiadasz uprawnienia do otwieranie skrzyn na terenie swojej gildii! &8(&7Popros lidera o uprawnienie&8)");
                    e.setCancelled(true);
                    return;
                }
                //TODO this.plugin.getGuildLogger().getLogger(GuildLogType.CHEST_OPEN).update(u, "");
                return;
            }
        }
    }
    @EventHandler
    public void onInteract11(PlayerInteractEvent e){
        for (SpecialItem specialItem : plugin.getSpecialItemManager().getSpecialItems().values()) {
            specialItem.openAction(e);
        }
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent e){
            final Player player = e.getPlayer();
            if(player.getItemInHand() != null){
                if(player.getItemInHand().getType().equals(Material.ENDER_PEARL)){
                    if(!plugin.getCoreConfig().TOGGLEMANAGER_PEARLS){
                        e.setCancelled(true);
                        sendMessage(player, "&4Blad: &cAktualnie rzucanie perel jest wylaczone");
                        return;
                    }
                    if(!e.getAction().equals(Action.LEFT_CLICK_AIR) && !e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                        if (!plugin.getCoreConfig().TOGGLEMANAGER_PEARLCOOLDOWN_STATUS) {
                            if (times.get(player.getUniqueId()) != null) {
                                if (System.currentTimeMillis() - times.get(player.getUniqueId()) < plugin.getCoreConfig().TOGGLEMANAGER_PEARLCOOLDOWN_SECONDS * 1000) {
                                    e.setCancelled(true);
                                    sendMessage(player, "&4Blad: &cNastepna perle mozesz rzucic za: &6" + (plugin.getCoreConfig().TOGGLEMANAGER_PEARLCOOLDOWN_SECONDS - (System.currentTimeMillis() - times.get(player)) / 1000L) + "s");
                                    return;
                                } else {
                                    times.remove(player.getUniqueId());
                                }
                            }
                            times.put(player.getUniqueId(), System.currentTimeMillis());
                        }
                    }
                    if(!plugin.getCoreConfig().TOGGLEMANAGER_PEARLSONAIR){
                        boolean status = false;
                        for(int i = 0 ; i < 3 ; i ++){
                            final Location location = player.getLocation().add(0,-(1+i), 0);
                            if(location.getBlock() != null && !location.getBlock().getType().equals(Material.AIR)){
                                status = true;
                            }
                        }
                        if(!status){
                            e.setCancelled(true);
                            sendMessage(player, "&4Blad: &cAktualnie rzucanie perel w powietrzu jest wylaczone");
                            return;
                        }
                    }
                }
                if(swords.contains(player.getItemInHand().getType())){
                    if(player.getItemInHand().getItemMeta().hasEnchant(Enchantment.KNOCKBACK)){
                        if(!plugin.getCoreConfig().TOGGLEMANAGER_KNOCKBACK){
                            player.getItemInHand().removeEnchantment(Enchantment.KNOCKBACK);
                            sendMessage(player, "&4Blad: &cKnockBack jest wylaczony na serwerze");
                        }
                    }
                }
                if(player.getItemInHand().getType().equals(Material.FISHING_ROD)){
                    final ItemStack item = player.getItemInHand();
                    if(item.getDurability() <= 44){
                        item.setDurability((short)45);
                        player.updateInventory();
                    }
                }
            }
    }
    @EventHandler
    public void onPlayerInteracts(PlayerInteractAtEntityEvent e){
        if(times(e.getPlayer()))return;
        if(e.getRightClicked() == null)return;
        if(!(e.getRightClicked() instanceof Player))return;
        final User user = plugin.getUserManager().getUser(e.getRightClicked().getUniqueId());
        if(user == null)return;
        ChatUtil.sendMessage(e.getPlayer(),"&4&lRANKING &8->> &7Zabijajac tego gracz uzyskasz: &2&l+&a&l" + DeathUtil.calculateAddRanking(plugin.getUserManager().getUser(e.getPlayer().getUniqueId()), user));
        ChatUtil.sendMessage(e.getPlayer(),"&4&lRANKING &8->> &7Jezeli ten gracz ciebie zabije stracisz: &c&l-&4&l" + DeathUtil.calculateRemoveRanking(user,plugin.getUserManager().getUser(e.getPlayer().getUniqueId())));
    }
    public void sendMessage(Player player, String message){
        final Long t = messageTimes.get(player.getUniqueId());
        if(t != null && System.currentTimeMillis() - t < 2000L){
            return;
        }
        ChatUtil.sendMessage(player, message);
        messageTimes.put(player.getUniqueId(), System.currentTimeMillis());
    }
    private boolean times(Player player){
        final Long t = messageTimes.get(player.getUniqueId());
        if(t != null && System.currentTimeMillis() - t < 2000L){
            return true;
        }
        messageTimes.put(player.getUniqueId(), System.currentTimeMillis());
        return false;
    }
}
