package pl.supereasy.sectors.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.build.enums.CanBuild;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.alliances.api.GuildAlliance;
import pl.supereasy.sectors.guilds.api.GuildMember;
import pl.supereasy.sectors.guilds.enums.AlliancePermission;
import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.HashMap;
import java.util.Map;

public class BucketListener implements Listener {

    private final SectorPlugin plugin;
    private final Map<Material, GuildPermission> permissions = new HashMap<Material, GuildPermission>() {{
        put(Material.LAVA_BUCKET, GuildPermission.LAVA_PLACE);
        put(Material.WATER_BUCKET, GuildPermission.WATER_PLACE);
    }};

    public BucketListener(SectorPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEmpty(PlayerBucketEmptyEvent e) {
        if (e.isCancelled()) return;
        final Player p = e.getPlayer();
        final User user = this.plugin.getUserManager().getUser(e.getPlayer().getUniqueId());
        final Guild g = this.plugin.getGuildManager().getGuild(e.getBlockClicked().getLocation());
        if (g == null) {
            return;
        }
        if (user.getGuild() == null) {
            p.sendBlockChange(e.getBlockClicked().getLocation(), e.getBlockClicked().getTypeId(), e.getBlockClicked().getData());
            e.setCancelled(true);
            return;
        }
        if (g.equals(user.getGuild())) {
            final GuildPermission permission = this.permissions.get(e.getBucket());
            final GuildMember guildMember = g.getMember(e.getPlayer().getUniqueId());
            if (permission != null && !g.isOwner(guildMember.getUUID()) && !guildMember.hasPermission(permission)) {
                e.getPlayer().sendMessage(ChatUtil.fixColor(" &8» &cNie mozesz tutaj tego wylac! &8(&7Popros lidera o uprawnienie&8)"));
                e.setCancelled(true);
                return;
            }
        } else {
            if (e.getBucket() == Material.WATER_BUCKET) {
                final GuildAlliance alliance = (GuildAlliance) this.plugin.getAllianceManager().findAllianceBetween(g, user.getGuild())
                        .markFirstGuild()
                        .findRelationGuildToMarked()
                        .getOrNull();
                if (alliance != null && alliance.hasPermission(AlliancePermission.WATER_PLACE)) {
                    return;
                } else {
                    p.sendBlockChange(e.getBlockClicked().getLocation(), e.getBlockClicked().getTypeId(), e.getBlockClicked().getData());
                    e.setCancelled(true);
                    return;
                }
            } else {
                e.setCancelled(true);
                return;
            }
        }
    }
    @EventHandler
    public void onEmptyInCenter(PlayerBucketEmptyEvent e) {
        if (e.isCancelled()) return;
        final User user = this.plugin.getUserManager().getUser(e.getPlayer().getUniqueId());
        if (user.getGuild() != null) {
            final Guild g = this.plugin.getGuildManager().getGuild(e.getBlockClicked().getLocation());
            if (g.equals(user.getGuild())) {
                if(g.getGuildRegion().isCenterWithAdd(e.getBlockClicked().getLocation(),6)){
                    e.getPlayer().sendMessage(ChatUtil.fixColor(" &8» &cNie mozesz tutaj tego wylac!)"));
                    e.setCancelled(true);
                }
            }

        }
    }
    @EventHandler
    public void onFill1(PlayerBucketFillEvent e){
        final Player player = e.getPlayer();
        final Block block = e.getBlockClicked();
        final User user = this.plugin.getUserManager().getUser(player.getUniqueId());
        final Guild guild = this.plugin.getGuildManager().getGuild(block.getLocation());
        final CanBuild canBuild = this.plugin.getBuildManager().isAllowBuild(player, user, block, guild);

        if (canBuild != CanBuild.ALLOW) {
            ChatUtil.sendMessage(player, canBuild.getMessage());
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onEmpty1(PlayerBucketEmptyEvent e){
        final Player player = e.getPlayer();
        final Block block = e.getBlockClicked();
        final User user = this.plugin.getUserManager().getUser(player.getUniqueId());
        final Guild guild = this.plugin.getGuildManager().getGuild(block.getLocation());
        final CanBuild canBuild = this.plugin.getBuildManager().isAllowBuild(player, user, block, guild);

        if (canBuild == CanBuild.NEAR_BORDER) {
            ChatUtil.sendMessage(player, canBuild.getMessage());
            e.setCancelled(true);
        }
    }

}
