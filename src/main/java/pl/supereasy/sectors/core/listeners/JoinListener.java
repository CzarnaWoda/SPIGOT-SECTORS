package pl.supereasy.sectors.core.listeners;

import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.supereasy.bpaddons.bossbar.*;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.blazingpack.cuboids.BlazingCuboidAPI;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.synchro.UserJoinedSectorPacket;
import pl.supereasy.sectors.api.packets.impl.user.UserRegisterPacket;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.api.sectors.type.SectorType;
import pl.supereasy.sectors.api.teleport.api.Teleportable;
import pl.supereasy.sectors.config.TabListConfig;
import pl.supereasy.sectors.core.combat.util.DeathUtil;
import pl.supereasy.sectors.core.commands.impl.global.BossBarCommand;
import pl.supereasy.sectors.core.tablist.AbstractTablist;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.core.user.impl.UserJoinManager;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.enums.RelationType;
import pl.supereasy.sectors.threads.api.ThreadExecutorAPI;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.TeleportUtil;
import pl.supereasy.sectors.util.TimeUtil;

import java.util.UUID;


public class JoinListener implements Listener {

    private final SectorPlugin plugin;

    public JoinListener(SectorPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        event.setJoinMessage(null);
        final Player p = event.getPlayer();
        this.plugin.getSectorManager().registerSectorChange(p.getUniqueId());
        User user = this.plugin.getUserManager().getUser(p.getUniqueId());
        if (plugin.getCombatManager().getCombat(p) == null) {
            plugin.getCombatManager().crateCombat(p);
        }else{
            DeathUtil.remove(plugin.getCombatManager().getCombat(p));
        }
        if (user == null) {
            //TODO pierwszy raz jest
            //final Sector sector = this.plugin.getSectorManager().getSector(event.getPlayer());
            final Sector currentSector = this.plugin.getSectorManager().getCurrentSector();
            user = new User(p, currentSector);
            user.saveUserToValues();
            user.insert(true);
            this.plugin.getSectorClient().sendGlobalPacket(new UserRegisterPacket(p.getUniqueId(), p.getName(), currentSector.getSectorName(), currentSector.getUniqueSectorID()));
            TeleportUtil.randomTeleport(p, currentSector.getMinX(), currentSector.getMaxX(), currentSector.getMinZ(), currentSector.getMaxZ());


        } else {
            if (!user.getName().equalsIgnoreCase(p.getName())) {
                user.setUserName(p.getName());
            }
            user.applyValuesToPlayer();
        }
        this.plugin.getSectorManager().getCurrentSector().addOnlinePlayer(user.getName());
        final Packet packet = new UserJoinedSectorPacket(user.getName(), user.getUUID(), this.plugin.getSectorManager().getCurrentSector().getSectorName());
        this.plugin.getSectorClient().sendPacket(packet);
        UserJoinManager.registerJoinNewly(user);
        //send packet
        //user.setUserSector(this.plugin.getSectorManager().getCurrentSector()); //TODO chwilowe -> wysylac wszedzie?

        //Teleportacja
        //TODO ************************ ZWERYFIKOWAC ***************************
        if (plugin.getSectorManager().canChangeSector(user.getUUID())) { ///TODO po co to jest?
            plugin.getSectorManager().registerSectorAction(user.getUUID());
        }

        final Teleportable teleportable = this.plugin.getTeleportManager().getRequest(user.getUUID());
        if (teleportable != null) {
            p.teleport(teleportable.getLocation());
            p.sendMessage(ChatUtil.fixColor("&8>> &7Teleportacja przebiegla pomyslnie!"));
            this.plugin.getTeleportManager().deleteRequestIfExists(user.getUUID());
        }
        /*if (user.getIncognito().isEnabled()) {
            SectorPlugin.getInstance().getIncognitoManager().refreshJoinIncognito(p, user);
        }*/
        user.setProtection(TimeUtil.parseDateDiff("3s", true));
        user.setLastJoin(System.currentTimeMillis());
        p.setFlying(user.isFlying());
        ThreadExecutorAPI.EXECUTOR.execute(() -> {
            final Sector currentSector = this.plugin.getSectorManager().getCurrentSector();
            if (currentSector.getSectorType() == SectorType.NORMAL) {
                BlazingCuboidAPI.reloadCuboids(p);
            } else if (currentSector.getSectorType() == SectorType.SPAWN) {
                BlazingCuboidAPI.resetCuboids(p);
            }
            ChatUtil.sendMessage(p, "&3&lSECTORS &8->> &7Dolaczyles na sektor &8(&c" + plugin.getSectorManager().getCurrentSector().getSectorName() + "&8)");
            plugin.getCoreConfig().JOINMANAGER_MESSAGE.forEach(message -> ChatUtil.sendMessage(p, ChatUtil.fixColor(message)));
            AbstractTablist abstractTablist = AbstractTablist.createTablist(TabListConfig.playerList, TabListConfig.TABLIST_HEADER, TabListConfig.TABLIST_FOOTER, 9999, p);
            abstractTablist.send();
            this.plugin.getTagManager().createBoard(p);
            this.plugin.getTagManager().updateBoard(p);
            SectorMoveListener.packet.setOperation(BarOperation.REMOVE);
            PlayerConnection conn = ((CraftPlayer) p).getHandle().playerConnection;
            conn.sendPacket(new PacketPlayOutCustomPayload("BP|UpdateBossInfo", SectorMoveListener.packet.serialize()));
            SectorMoveListener.bossBars.remove(p);
            PlayerCommandPreprocessListener.afterJoinTimes.put(p, System.currentTimeMillis());
            if (!plugin.getSectorManager().getCurrentSector().entityInSector(p.getLocation())) {
                plugin.getTeleportManager().teleportToSpawn(p, true);
            }
            plugin.getEasyScoreboardManager().createScoreBoard(p);
            BossBarCommand.packet.setOperation(BarOperation.REMOVE);
            conn.sendPacket(new PacketPlayOutCustomPayload("BP|UpdateBossInfo", BossBarCommand.packet.serialize()));
            if (BossBarCommand.enable) {
                BossBarCommand.packet.setOperation(BarOperation.ADD);
                conn.sendPacket(new PacketPlayOutCustomPayload("BP|UpdateBossInfo", BossBarCommand.packet.serialize()));
            }
            SectorMoveListener.cuboidPacket.setOperation(BarOperation.REMOVE);
            conn.sendPacket(new PacketPlayOutCustomPayload("BP|UpdateBossInfo", SectorMoveListener.cuboidPacket.serialize()));
            if(plugin.getSectorManager().getCurrentSector().getSectorType() != SectorType.SPAWN){
                final Guild guild = plugin.getGuildManager().getGuild(p.getLocation());
                if(guild != null){
                    final RelationType relationType = plugin.getGuildManager().getRelation(plugin.getUserManager().getUser(p.getUniqueId()).getGuild(),guild);
                    final Location center = guild.getGuildRegion().getCenter();
                    BossBarPacket cuboidPacket = BossBarBuilder.add(UUID.fromString("c5d878d8-b779-11ec-b909-0242ac120002"))
                            .style(BarStyle.SEGMENTED_12)
                            .color((relationType  == RelationType.ENEMY ? BarColor.RED : relationType == RelationType.ALLY ? BarColor.YELLOW : BarColor.GREEN))
                            .progress((float) (p.getLocation().distance(center)/guild.getGuildSize()))
                            .title(TextComponent.fromLegacyText(ChatUtil.fixColor(relationType.getColor() + "WKROCZYLES NA TEREN GILDII [" + guild.getTag() + "]")))
                            .buildPacket();

                    cuboidPacket.setOperation(BarOperation.ADD);
                    conn.sendPacket(new PacketPlayOutCustomPayload("BP|UpdateBossInfo", cuboidPacket.serialize()));
                }
            }
        });
    }

}
