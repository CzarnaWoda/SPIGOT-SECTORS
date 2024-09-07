package pl.supereasy.sectors.api.teleport.impl;

import com.google.common.cache.*;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import pl.supereasy.bpaddons.coords.CoordsUtil;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.api.sectors.type.SectorType;
import pl.supereasy.sectors.api.teleport.api.Teleportable;
import pl.supereasy.sectors.api.teleport.api.TeleportManager;
import pl.supereasy.sectors.api.teleport.api.TeleportRequest;
import pl.supereasy.sectors.api.teleport.api.TimerCallback;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.core.user.impl.UserJoinManager;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.TeleportUtil;
import pl.supereasy.sectors.util.TimeUtil;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class TeleportManagerImpl implements TeleportManager {

    private final SectorPlugin plugin;
    private final Map<UUID, TimerTask> tasks = new HashMap<>();
    private final Cache<UUID, Teleportable> teleportableCache;
    public static final Cache<UUID,Long> cancelActionCache = CacheBuilder.newBuilder().expireAfterWrite(8,TimeUnit.SECONDS).build();


    public TeleportManagerImpl(SectorPlugin plugin) {
        this.plugin = plugin;
        this.teleportableCache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.SECONDS).build();
    }


    @Override
    public void teleportPlayer(User teleportUser, User targetUser ,boolean instant) {
        final Player teleportPlayer = teleportUser.asPlayer();
        if (teleportPlayer == null || !teleportPlayer.isOnline()) {
            return;
        }
        final TeleportRequest teleportRequest = new PlayerTeleport(this.plugin, teleportUser, targetUser);
        if (!targetUser.getSector().isSectorOnline()) {
            ChatUtil.sendMessage(teleportPlayer, "&8» &7Docelowy sektor jest wylaczony (&c" + targetUser.getSector().getSectorName() + "&7)");
            return;
        }
        final boolean hasPermission = teleportUser.getGroup().hasPermission(UserGroup.HELPER) || instant;
        if (!hasPermission && this.plugin.getCombatManager().getCombat(teleportPlayer).hasFight()) {
            ChatUtil.sendMessage(teleportPlayer, "&8» &7Teleportacja anulowana (&cJestes podczas walki!&7)");
            return;
        }
        if (hasPermission) {
            teleportRequest.reuqestAccepted();
            return;
        }
        ChatUtil.sendMessage(teleportPlayer, "&8» &eTeleportacja nastapi za 5 sekund!");
        cancelActionCache.put(teleportPlayer.getUniqueId(),System.currentTimeMillis());
        MinecraftServer.getServer().postToMainThread(() -> {
            teleportPlayer.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 5 * 20, 0), true);
            teleportPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5 * 20, 0), true);
            runNow(teleportUser, new TimerCallback<Teleportable>() {
                @Override
                public void success() {
                    teleportPlayer.removePotionEffect(PotionEffectType.CONFUSION);
                    teleportPlayer.removePotionEffect(PotionEffectType.BLINDNESS);
                    teleportRequest.reuqestAccepted();
                    targetUser.removeTpaRequest(teleportUser.getName());
                }

                @Override
                public void error() {
                    teleportPlayer.removePotionEffect(PotionEffectType.CONFUSION);
                    teleportPlayer.removePotionEffect(PotionEffectType.BLINDNESS);
                    ChatUtil.sendMessage(teleportPlayer, " &8» &7Teleportacja zostala anulowana. &8(&7Powod: &cRuszyles sie!&8)");
                }
            });
        });
    }

    private void runNow(User user, TimerCallback<Teleportable> call) {
        TimerTask timerTask = tasks.get(user.getUUID());
        if (timerTask != null) {
            timerTask.cancel();
            return;
        }
        timerTask = new TimerTask(user.getUUID(), call);
        tasks.put(user.getUUID(), timerTask);
        timerTask.runTaskLater(this.plugin, 100);
    }

    @Override
    public void teleportPlayer(User teleportUser, Location location, Sector sector) {
        final Player teleportPlayer = teleportUser.asPlayer();
        if (teleportPlayer == null || !teleportPlayer.isOnline()) {
            return;
        }
        final TeleportRequest teleportRequest = new LocationTeleport(this.plugin, teleportUser, sector, location);
        if (!sector.isSectorOnline()) {
            ChatUtil.sendMessage(teleportPlayer, "&8» &7Docelowy sektor jest wylaczony (&c" + sector.getSectorName() + "&7)");
            return;
        }
        final boolean hasPermission = teleportUser.getGroup().hasPermission(UserGroup.HELPER);
        if (!hasPermission && this.plugin.getCombatManager().getCombat(teleportPlayer).hasFight()) {
            ChatUtil.sendMessage(teleportPlayer, "&8» &7Teleportacja anulowana (&cJestes podczas walki!&7)");
            return;
        }
        if (hasPermission) {
            teleportRequest.reuqestAccepted();
            return;
        }
        ChatUtil.sendMessage(teleportPlayer, "&8» &eTeleportacja nastapi za 5 sekund!");
        cancelActionCache.put(teleportPlayer.getUniqueId(),System.currentTimeMillis());
        teleportPlayer.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 5 * 20, 0), true);
        teleportPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5 * 20, 0), true);
        runNow(teleportUser, new TimerCallback<Teleportable>() {
            @Override
            public void success() {
                teleportPlayer.removePotionEffect(PotionEffectType.CONFUSION);
                teleportPlayer.removePotionEffect(PotionEffectType.BLINDNESS);
                teleportRequest.reuqestAccepted();
            }

            @Override
            public void error() {
                teleportPlayer.removePotionEffect(PotionEffectType.CONFUSION);
                teleportPlayer.removePotionEffect(PotionEffectType.BLINDNESS);
                ChatUtil.sendMessage(teleportPlayer, " &8» &7Teleportacja zostala anulowana. &8(&7Powod: &cRuszyles sie!&8)");
            }
        });
    }


    @Override
    public void deleteRequestIfExists(UUID uuid) {
        TimerTask t = tasks.get(uuid);
        if (t != null) {
            cancel(t, uuid);
        }
    }

    @Override
    public boolean hasTeleportRequest(UUID uuid) {
        return this.tasks.get(uuid) != null;
    }

    @Override
    public Teleportable getRequest(UUID uuid) {
        return this.teleportableCache.asMap().get(uuid);//TODO asMap
    }

    @Override
    public void registerRequest(UUID requestedPlayer, Teleportable teleportable) {
        this.teleportableCache.put(requestedPlayer, teleportable);
    }

    @Override
    public void teleportToSpawn(User user, Player player, boolean instant) {
        final long time = System.currentTimeMillis();
        final Optional<Sector> optSector = this.plugin.getSectorManager().getSectors().values()
                .stream()
                .filter(sec -> sec.getSectorType() == SectorType.SPAWN)
                .filter(sec -> time - sec.getLastUpdate() <= 7000)
                .min(Comparator.comparing(sec -> sec.getOnlinePlayers().size()));
        if (!optSector.isPresent()) {
            ChatUtil.sendMessage(player, "&7Brak aktualnie spawna na ktory mozemy Cie przeniesc :(");
            return;
        }
        final Sector sector = optSector.get();
        final TeleportRequest teleportRequest = new SpawnTeleport(this.plugin, user, sector);
        if (instant || user.getGroup().hasPermission(UserGroup.HELPER)) {
            teleportRequest.reuqestAccepted();
            return;
        }
        ChatUtil.sendMessage(player, "&8» &eTeleportacja nastapi za 5 sekund!");
        cancelActionCache.put(player.getUniqueId(),System.currentTimeMillis());
        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 5 * 20, 0), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5 * 20, 0), true);
        runNow(user, new TimerCallback<Teleportable>() {
            @Override
            public void success() {
                CoordsUtil.showCoords(player);
                player.removePotionEffect(PotionEffectType.CONFUSION);
                player.removePotionEffect(PotionEffectType.BLINDNESS);
                teleportRequest.reuqestAccepted();
            }

            @Override
            public void error() {
                player.removePotionEffect(PotionEffectType.CONFUSION);
                player.removePotionEffect(PotionEffectType.BLINDNESS);
                ChatUtil.sendMessage(player, " &8» &7Teleportacja zostala anulowana. &8(&7Powod: &cRuszyles sie!&8)");
            }
        });
    }

    @Override
    public void teleportToRandomSector(User user, Player player) {
        final long time = System.currentTimeMillis();
        final Optional<Sector> optSector = this.plugin.getSectorManager().getSectors().values()
                .stream()
                .filter(sec -> sec.getSectorType() == SectorType.NORMAL)
                .filter(sec -> time - sec.getLastUpdate() <= 7000)
                .min(Comparator.comparing(sec -> sec.getOnlinePlayers().size()));
        if (!optSector.isPresent()) {
            ChatUtil.sendMessage(player, "&7Brak aktualnie sektora na ktory mozemy Cie przeniesc :(");
            return;
        }
        final Sector sector = optSector.get();
        if (user.getSector().getUniqueSectorID() == sector.getUniqueSectorID()) {
            ChatUtil.sendMessage(player, " &7Jestes juz polaczony z sektorem &e" + sector.getSectorName());
            return;
        }
        final TeleportRequest teleportRequest = new RandomLocationTeleport(this.plugin, user, sector);
        ChatUtil.sendMessage(player, " &7Trwa laczenie z sektorem &e" + sector.getSectorName());
        teleportRequest.reuqestAccepted();
    }

    @Override
    public void teleportPlayersToRandomSector(List<User> users) {
        final long time = System.currentTimeMillis();
        final Optional<Sector> optSector = this.plugin.getSectorManager().getSectors().values()
                .stream()
                .filter(sec -> sec.getSectorType() == SectorType.NORMAL)
                .filter(sec -> time - sec.getLastUpdate() <= 7000)
                .min(Comparator.comparing(sec -> sec.getOnlinePlayers().size()));
        if (!optSector.isPresent()) {
            for (User user : users) {
                final Player player = user.asPlayer();
                if (player != null && player.isOnline()) {
                    ChatUtil.sendMessage(player, "&7Brak aktualnie sektora na ktory mozemy Cie przeniesc :(");
                }
            }
            return;
        }
        final Sector sector = optSector.get();
        for (User user : users) {
            if (user.getSector().getUniqueSectorID() == sector.getUniqueSectorID()) {
                ChatUtil.sendMessage(user.asPlayer(), " &7Jestes juz polaczony z sektorem &e" + sector.getSectorName());
                return;
            }
        }
        final Location location = TeleportUtil.randomLocation(sector.getMinX(),sector.getMaxX(),sector.getMinZ(),sector.getMaxZ());
        for (User user : users) {
            final Player player = user.asPlayer();
            user.setPotionEffects(Collections.singletonList(new PotionEffect(PotionEffectType.SPEED,1,0)));
            if (player != null && player.isOnline()) {
                player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
                CoordsUtil.hideCoords(player);
            }
            final TeleportRequest teleportRequest = new LocationTeleport(this.plugin, user, sector, location);
            ChatUtil.sendMessage(user.asPlayer(), " &7Trwa laczenie z sektorem &e" + sector.getSectorName());
            teleportRequest.reuqestAccepted();
        }
    }


    private void cancel(TimerTask task, UUID uuid) {
        task.cancel();
        tasks.remove(uuid);
    }

    public class TimerTask
            extends BukkitRunnable {
        private final UUID player;
        private final TimerCallback<Teleportable> call;

        public void run() {
            this.call.success();
            tasks.remove(this.player);
        }

        public void cancel() {
            super.cancel();
            this.call.error();
        }

        public TimerTask(UUID player, TimerCallback<Teleportable> call) {
            this.player = player;
            this.call = call;
        }

        public UUID getPlayer() {
            return this.player;
        }
    }

}
