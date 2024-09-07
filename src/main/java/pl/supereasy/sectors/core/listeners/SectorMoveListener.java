package pl.supereasy.sectors.core.listeners;

import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import pl.supereasy.bpaddons.bossbar.*;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.enums.RelationType;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.Time;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SectorMoveListener implements Listener {

    private final SectorPlugin plugin;
    public final static List<Player> bossBars = new ArrayList<>();
    public final static BossBarPacket packet = BossBarBuilder.add(UUID.fromString("c58a2578-9a8f-420d-b4e1-35b8a10adf31"))
            .style(BarStyle.SOLID)
            .color(BarColor.PURPLE)
            .title(TextComponent.fromLegacyText(ChatUtil.fixColor("&8->> ( &aGRANICA SEKTORA &a&n" + SectorPlugin.getInstance().getSectorManager().getCurrentSector().getSectorName().toUpperCase() + "&8 ) <<-")))
            .buildPacket();
    private final UUID intruderUUID;

    public static BossBarPacket cuboidPacket = BossBarBuilder.add(UUID.fromString("c5d878d8-b779-11ec-b909-0242ac120002"))
            .style(BarStyle.SEGMENTED_12)
            .color(BarColor.PURPLE)
            .progress(1)
            .title(TextComponent.fromLegacyText(ChatUtil.fixColor("GILDIA")))
            .buildPacket();
    public SectorMoveListener(SectorPlugin plugin) {
        this.plugin = plugin;
        this.intruderUUID = UUID.randomUUID();
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final int xfrom = e.getFrom().getBlockX();
        final int zfrom = e.getFrom().getBlockZ();
        final int yfrom = e.getFrom().getBlockY();
        final int xto = e.getTo().getBlockX();
        final int yto = e.getTo().getBlockY();
        final int zto = e.getTo().getBlockZ();
        if (xfrom != xto || zfrom != zto || yfrom != yto) {
            final Location to = e.getTo();

            final double distance = (int) plugin.getSectorManager().getCurrentSector().howCloseBorderInSector(to);

            final Player player = e.getPlayer();
            final PlayerConnection conn = ((CraftPlayer) player).getHandle().playerConnection;
            if (distance <= 30) {
                final float procent = (float) (distance/ (30.0));
                packet.setProgress(procent);
                packet.setOperation(BarOperation.UPDATE_PCT);
                conn.sendPacket(new PacketPlayOutCustomPayload("BP|UpdateBossInfo", packet.serialize()));
                if (!bossBars.contains(player)) {
                    packet.setOperation(BarOperation.ADD);
                    conn.sendPacket(new PacketPlayOutCustomPayload("BP|UpdateBossInfo", packet.serialize()));
                    bossBars.add(player);
                }
                return;
            } else {
                if (bossBars.contains(player)) {
                    packet.setOperation(BarOperation.REMOVE);
                    conn.sendPacket(new PacketPlayOutCustomPayload("BP|UpdateBossInfo", packet.serialize()));
                    bossBars.remove(player);
                }
            }
            final Location from = e.getFrom();
            final Guild guildFrom = this.plugin.getGuildManager().getGuild(from);
            final Guild guildTo = this.plugin.getGuildManager().getGuild(to);
            if (guildFrom != null && guildTo == null) {
                cuboidPacket.setOperation(BarOperation.REMOVE);
                conn.sendPacket(new PacketPlayOutCustomPayload("BP|UpdateBossInfo", cuboidPacket.serialize()));
                e.getPlayer().sendTitle("", ChatUtil.fixColor(" &7Opusciles teren gildii &8[&2" + guildFrom.getTag() + "&8]"));
                return;
            }
            if (guildTo != null && guildFrom == null) {
                final User user = plugin.getUserManager().getUser(player.getUniqueId());
                final RelationType relationType = plugin.getGuildManager().getRelation(user.getGuild(), guildTo);
                final Location center = guildTo.getGuildRegion().getCenter();
                center.setY(yto);

                cuboidPacket = BossBarBuilder.add(UUID.fromString("c5d878d8-b779-11ec-b909-0242ac120002"))
                        .style(BarStyle.SEGMENTED_12)
                        .color((relationType == RelationType.ENEMY ? BarColor.RED : relationType == RelationType.ALLY ? BarColor.YELLOW : BarColor.GREEN))
                        .progress((float) (to.distance(center) / guildTo.getGuildSize()))
                        .title(TextComponent.fromLegacyText(ChatUtil.fixColor(relationType.getColor() + "WKROCZYLES NA TEREN GILDII [" + guildTo.getTag() + "]")))
                        .buildPacket();

                cuboidPacket.setOperation(BarOperation.ADD);
                conn.sendPacket(new PacketPlayOutCustomPayload("BP|UpdateBossInfo", cuboidPacket.serialize()));

                e.getPlayer().sendTitle("", ChatUtil.fixColor(" &7Wkroczyles na teren gildii &8[&2" + guildTo.getTag() + "&8]"));
                if (guildTo.getGuildCreateTime() + Time.HOUR.getTime(24) > System.currentTimeMillis()) {
                    ChatUtil.sendMessage(player, "&8->> &cTa gildia posiada ochrone przed &4&lTNT");
                }
                Guild playerGuild = this.plugin.getGuildManager().getGuild(e.getPlayer().getUniqueId());
                if (playerGuild != null && playerGuild.equals(guildTo)) {
                    return;
                }
                if (!plugin.getVanishManager().getVanished().contains(e.getPlayer())) {
                    for (Player onlineMember : guildTo.getOnlineMembersAsPlayers()) {
                        new BlazingBossBar(BossBarBuilder.add(this.intruderUUID)
                                .style(BarStyle.SOLID)
                                .color(BarColor.RED)
                                .progress(1)
                                .title(TextComponent.fromLegacyText(ChatUtil.fixColor(" &8» &4&lUWAGA! &7intruz &c" + e.getPlayer().getDisplayName() + "&7 na terenie gildi!")))
                                .buildPacket()
                        ).sendNotification(onlineMember, 5);
                    }
                }
            }
            if(guildTo != null && guildFrom != null && guildTo.equals(guildFrom)){
                final Location center = guildTo.getGuildRegion().getCenter();
                center.setY(yto);

                cuboidPacket.setProgress((float) (to.distance(center)/guildTo.getGuildSize()));
                cuboidPacket.setOperation(BarOperation.UPDATE_PCT);
                conn.sendPacket(new PacketPlayOutCustomPayload("BP|UpdateBossInfo", cuboidPacket.serialize()));
            }
        }
    }
}
