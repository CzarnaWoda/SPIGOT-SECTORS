package pl.supereasy.sectors.core.listeners;

import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import pl.supereasy.bpaddons.bossbar.*;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.user.UserThrowPearlPacket;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.enums.RelationType;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.UUID;

public class PlayerTeleportListener implements Listener {

    private final SectorPlugin plugin;

    public PlayerTeleportListener(SectorPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if (e.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) return;
        final Packet packet = new UserThrowPearlPacket(e.getPlayer().getUniqueId());
        this.plugin.getSectorClient().sendGlobalPacket(packet);
    }
    @EventHandler
    public void onTeleportToGuild(PlayerTeleportEvent e){
        if(e.isCancelled())return;
        final Player player = e.getPlayer();
        final PlayerConnection conn = ((CraftPlayer) player).getHandle().playerConnection;
        if(plugin.getGuildManager().getGuild(e.getFrom()) != null && plugin.getGuildManager().getGuild(e.getTo()) == null){
            SectorMoveListener.cuboidPacket.setOperation(BarOperation.REMOVE);
            conn.sendPacket(new PacketPlayOutCustomPayload("BP|UpdateBossInfo", SectorMoveListener.cuboidPacket.serialize()));
        }
        if(plugin.getGuildManager().getGuild(e.getFrom()) == null && plugin.getGuildManager().getGuild(e.getTo()) != null){

            final Guild guild = plugin.getGuildManager().getGuild(e.getTo());
            final RelationType relationType = plugin.getGuildManager().getRelation(plugin.getUserManager().getUser(player.getUniqueId()).getGuild(),guild);
            final Location center = guild.getGuildRegion().getCenter();
            BossBarPacket cuboidPacket = BossBarBuilder.add(UUID.fromString("c5d878d8-b779-11ec-b909-0242ac120002"))
                    .style(BarStyle.SEGMENTED_12)
                    .color((relationType  == RelationType.ENEMY ? BarColor.RED : relationType == RelationType.ALLY ? BarColor.YELLOW : BarColor.GREEN))
                    .progress((float) (player.getLocation().distance(center)/guild.getGuildSize()))
                    .title(TextComponent.fromLegacyText(ChatUtil.fixColor(relationType.getColor() + "WKROCZYLES NA TEREN GILDII [" + guild.getTag() + "]")))
                    .buildPacket();

            cuboidPacket.setOperation(BarOperation.ADD);
            conn.sendPacket(new PacketPlayOutCustomPayload("BP|UpdateBossInfo", cuboidPacket.serialize()));
        }
        if(plugin.getGuildManager().getGuild(e.getFrom()) != null && plugin.getGuildManager().getGuild(e.getTo()) != null){
            SectorMoveListener.cuboidPacket.setOperation(BarOperation.REMOVE);
            conn.sendPacket(new PacketPlayOutCustomPayload("BP|UpdateBossInfo", SectorMoveListener.cuboidPacket.serialize()));

            final Guild guild = plugin.getGuildManager().getGuild(e.getTo());
            final RelationType relationType = plugin.getGuildManager().getRelation(plugin.getUserManager().getUser(player.getUniqueId()).getGuild(),guild);
            final Location center = guild.getGuildRegion().getCenter();
            BossBarPacket cuboidPacket = BossBarBuilder.add(UUID.fromString("c5d878d8-b779-11ec-b909-0242ac120002"))
                    .style(BarStyle.SEGMENTED_12)
                    .color((relationType  == RelationType.ENEMY ? BarColor.RED : relationType == RelationType.ALLY ? BarColor.YELLOW : BarColor.GREEN))
                    .progress((float) (player.getLocation().distance(center)/guild.getGuildSize()))
                    .title(TextComponent.fromLegacyText(ChatUtil.fixColor(relationType.getColor() + "WKROCZYLES NA TEREN GILDII [" + guild.getTag() + "]")))
                    .buildPacket();

            cuboidPacket.setOperation(BarOperation.ADD);
            conn.sendPacket(new PacketPlayOutCustomPayload("BP|UpdateBossInfo", cuboidPacket.serialize()));
        }
    }
}
