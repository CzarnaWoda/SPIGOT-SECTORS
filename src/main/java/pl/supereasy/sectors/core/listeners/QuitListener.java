package pl.supereasy.sectors.core.listeners;

import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.bpaddons.bossbar.BarOperation;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.chat.BroadcastChatMessage;
import pl.supereasy.sectors.api.packets.impl.synchro.UserChangeSectorPacket;
import pl.supereasy.sectors.api.packets.impl.synchro.UserLeftSectorPacket;
import pl.supereasy.sectors.api.packets.impl.user.UserKillPacket;
import pl.supereasy.sectors.api.packets.impl.user.UserLogoutPacket;
import pl.supereasy.sectors.api.packets.impl.user.UserSpawnTeleport;
import pl.supereasy.sectors.api.sectors.borderapi.tasks.BorderTask;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.core.combat.data.Combat;
import pl.supereasy.sectors.core.combat.util.DeathUtil;
import pl.supereasy.sectors.core.tablist.AbstractTablist;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.util.BossBarUtil;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemUtil;
import pl.supereasy.sectors.util.TeleportUtil;

import javax.swing.border.Border;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuitListener implements Listener {

    private final SectorPlugin plugin;

    public QuitListener(SectorPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        event.setQuitMessage(null);
        plugin.getEasyScoreboardManager().removeScoreBoard(event.getPlayer());

        final User user = this.plugin.getUserManager().getUser(event.getPlayer().getUniqueId());
        if (user == null) {
            return;
        }
        this.plugin.getSectorManager().getCurrentSector().removeOnlinePlayer(user.getName());
        final Player p = event.getPlayer();
        final Combat c = plugin.getCombatManager().getCombat(event.getPlayer());
        if(c != null && c.hasFight()){
            p.setHealth(0.0D);
            DeathUtil.remove(c);
            final Sector randomSpawn = TeleportUtil.getRandomSpawn();
            final Packet packet = new UserLogoutPacket(p.getUniqueId(), (randomSpawn == null ? "spawn-1" : randomSpawn.getSectorName()));
            plugin.getSectorClient().sendGlobalPacket(packet); //AntyLogout

            //TODO (?)
        }

        final Guild guild = this.plugin.getGuildManager().getGuild(p.getLocation());
        boolean shouldTeleportToSpawn = !user.getGroup().hasPermission(UserGroup.ADMIN) && guild != null && (user.getGuild() == null || user.getGuild() != null && !user.getGuild().getTag().equalsIgnoreCase(guild.getTag())) && p.getLocation().distance(guild.getGuildRegion().getCenter()) <= 15;
        String spawnName = "spawn-1";
        if (shouldTeleportToSpawn) {
            final Sector spawn = TeleportUtil.getRandomSpawn();
            spawnName = (spawn == null ? "spawn-1" : spawn.getSectorName());
            Packet packet = new UserSpawnTeleport(p.getName(), spawnName);
            this.plugin.getSectorClient().sendGlobalPacket(packet);
            p.setHealth(20);
            p.setFoodLevel(20);
        }
        user.saveUserToValues();
        user.insert(true);
        if (shouldTeleportToSpawn) {
            Packet packet = new UserChangeSectorPacket(user);
            this.plugin.getSectorClient().sendUser(packet, spawnName);
        }

        //*** TELEPORT REQUEST CANCEL ***//
        this.plugin.getTeleportManager().deleteRequestIfExists(event.getPlayer());

        AbstractTablist.removeTablist(p);
        this.plugin.getTagManager().deleteBoard(p);
        /*final Packet packet = new UserLeftSectorPacket(user.getName(), this.plugin.getSectorManager().getCurrentSector().getSectorName());
        this.plugin.getSectorClient().sendPacket(packet);*/
        PlayerConnection conn = ((CraftPlayer) p).getHandle().playerConnection;

        SectorMoveListener.packet.setOperation(BarOperation.REMOVE);
        conn.sendPacket(new PacketPlayOutCustomPayload("BP|UpdateBossInfo", SectorMoveListener.packet.serialize()));
        SectorMoveListener.bossBars.remove(p);

        SectorMoveListener.cuboidPacket.setOperation(BarOperation.REMOVE);
        conn.sendPacket(new PacketPlayOutCustomPayload("BP|UpdateBossInfo", SectorMoveListener.cuboidPacket.serialize()));
    }

}
