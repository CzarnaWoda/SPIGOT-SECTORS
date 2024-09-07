package pl.supereasy.sectors.api.packets.impl;

import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.supereasy.bpaddons.bossbar.BarColor;
import pl.supereasy.bpaddons.bossbar.BarOperation;
import pl.supereasy.bpaddons.bossbar.BarStyle;
import pl.supereasy.bpaddons.bossbar.BossBarBuilder;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.errors.ReportError;
import pl.supereasy.sectors.api.packets.api.PacketHandler;
import pl.supereasy.sectors.api.packets.impl.admin.AddCoinsPacket;
import pl.supereasy.sectors.api.packets.impl.chat.*;
import pl.supereasy.sectors.api.packets.impl.configs.*;
import pl.supereasy.sectors.api.packets.impl.conn.SectorDisablePacket;
import pl.supereasy.sectors.api.packets.impl.conn.SectorRegisterPacket;
import pl.supereasy.sectors.api.packets.impl.events.GlobalEventPacket;
import pl.supereasy.sectors.api.packets.impl.guild.*;
import pl.supereasy.sectors.api.packets.impl.player.*;
import pl.supereasy.sectors.api.packets.impl.proxies.ProxyStatusPacket;
import pl.supereasy.sectors.api.packets.impl.request.RequestOnlineList;
import pl.supereasy.sectors.api.packets.impl.global.*;
import pl.supereasy.sectors.api.packets.impl.status.ServerStatusPacket;
import pl.supereasy.sectors.api.packets.impl.synchro.*;
import pl.supereasy.sectors.api.packets.impl.tops.TopPacket;
import pl.supereasy.sectors.api.packets.impl.user.*;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.api.teleport.impl.TeleportableImpl;
import pl.supereasy.sectors.config.CoreConfig;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.config.enums.Config;
import pl.supereasy.sectors.core.commands.impl.global.BossBarCommand;
import pl.supereasy.sectors.core.events.EventUtil;
import pl.supereasy.sectors.core.events.enums.EventType;
import pl.supereasy.sectors.core.home.impl.HomeImpl;
import pl.supereasy.sectors.core.kits.data.Kit;
import pl.supereasy.sectors.core.tnt.TntManager;
import pl.supereasy.sectors.core.user.enums.MessageType;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.core.warp.api.Warp;
import pl.supereasy.sectors.core.warp.impl.WarpImpl;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.alliances.api.Alliance;
import pl.supereasy.sectors.guilds.alliances.api.GuildAlliance;
import pl.supereasy.sectors.guilds.api.GuildMember;
import pl.supereasy.sectors.guilds.enums.AlliancePermission;
import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.guilds.enums.RelationType;
import pl.supereasy.sectors.guilds.impl.GuildMemberImpl;
import pl.supereasy.sectors.guilds.inventories.AllianceManageInventory;
import pl.supereasy.sectors.guilds.inventories.MemberPermissionInventory;
import pl.supereasy.sectors.guilds.log.impl.GuildLogActionImpl;
import pl.supereasy.sectors.guilds.wars.api.War;
import pl.supereasy.sectors.proxies.Proxy;
import pl.supereasy.sectors.proxies.ProxyManager;
import pl.supereasy.sectors.util.*;
import pl.supereasy.sectors.util.title.TitleUtil;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public class PacketHandlerImpl implements PacketHandler {

    private final SectorPlugin plugin;

    public PacketHandlerImpl(SectorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(UserRegisterPacket userRegisterPacket) {
        final Sector sector = this.plugin.getSectorManager().getSector(userRegisterPacket.getSectorID());
        if (sector == null) {
            ReportError.save(userRegisterPacket.getSectorID() + " byl nullem, nie stworzylo gracza!");
            throw new RuntimeException("Helikopter wyladowal na sektorze ktorego nie ma!");
        }
        final User user = new User(userRegisterPacket.getUuid(), userRegisterPacket.getUserName(), sector);
        user.setUserSector(sector);
        this.plugin.getUserManager().registerUser(user);
        user.getSector().getOnlinePlayers().add(user.getName());
        this.plugin.getLOGGER().log(Level.INFO, "Zarejestrowano gracza " + user.getName() + " sektor: " + user.getSector().getSectorName());
    }

    @Override
    public void handle(GlobalChatMessage globalChatMessage) {
        final User user = this.plugin.getUserManager().getUser(globalChatMessage.getSenderUUID());
        Guild g = user.getGuild();
        for (final Player p : Bukkit.getOnlinePlayers()) {
            final User receiveUser = this.plugin.getUserManager().getUser(p.getUniqueId());
            final RelationType type = this.plugin.getGuildManager().getRelation(receiveUser, user);
            String tag = "";
            if (g != null) {
                tag = ChatColor.DARK_GRAY + "[" + type.getColor() + g.getTag() + ChatColor.DARK_GRAY + "] " + ChatColor.WHITE;
            }
            String messageFormat = this.plugin.getCoreConfig().globalChatMessageFormat;
            messageFormat = messageFormat.replace("{GUILD}", (g == null ? "" : tag));
            messageFormat = messageFormat.replace("{POINTS}", String.valueOf(user.getPoints()));
            messageFormat = messageFormat.replace("{RANK}", user.getGroup().getGroupPrefix());
            messageFormat = messageFormat.replace("{PLAYER}", user.getName());
            messageFormat = messageFormat.replace("{MESSAGE}", globalChatMessage.getChatMessage());
            messageFormat = messageFormat.replace("{SUFFIX}", user.getGroup().getSuffix());
            p.sendMessage(ChatUtil.fixColor(messageFormat));
        }
    }

    @Override
    public void handle(BroadcastChatMessage broadcastChatMessage) {
        Bukkit.broadcastMessage(broadcastChatMessage.getChatMessage());
    }

    @Override
    public void handle(SpecifiedMessageTypePacket specifiedMessageTypePacket) {
        final MessageType messageType = MessageType.valueOf(specifiedMessageTypePacket.getMessageType());
        for (final Player p : Bukkit.getOnlinePlayers()) {
            final User user = this.plugin.getUserManager().getUser(p.getUniqueId());
            if (!user.getChat().hasDisabled(messageType)) {
                p.sendMessage(ChatUtil.fixColor(specifiedMessageTypePacket.getMessage()));
            }
        }
    }

    @Override
    public void handle(UpdateAssistsPacket updateAssistsPacket) {
    }

    @Override
    public void handle(UserLogoutPacket userLogoutPacket) {
        final User u = this.plugin.getUserManager().getUser(userLogoutPacket.getUuid());
        if (u != null) {
            u.setUserLocation(Bukkit.getWorld("world").getSpawnLocation());
        }
    }


    @Override
    public void handle(UserDeathPacket userDeathPacket) {
        final User user = this.plugin.getUserManager().getUser(userDeathPacket.getUuid());
        if (user == null) {
            ReportError.save("UserDeathPacket " + userDeathPacket.getUuid() + " jest nullem!");
            return;
        }
        user.addDeaths(1);
    }

    @Override
    public void handle(UserKillPacket userKillPacket) {
        final User kUser = this.plugin.getUserManager().getUser(userKillPacket.getAttackerUUID());
        final User victimUser = this.plugin.getUserManager().getUser(userKillPacket.getVictimUUID());
        kUser.addPoints(userKillPacket.getAttackAddPoints());
        kUser.setLastKill(victimUser.getName());
        kUser.setLastKillTime(TimeUtil.parseDateDiff("45m",true));
        kUser.addKills(1);
        victimUser.removePoints(userKillPacket.getVictimRemovePoints());
        victimUser.addDeaths(1);
        if (kUser.getGuild() != null && victimUser.getGuild() != null) {
            final War war = this.plugin.getWarManager().getWar(kUser.getGuild(), victimUser.getGuild());
            if (war != null) {
                if (war.getFirstGuild().getTag().equalsIgnoreCase(kUser.getGuild().getTag())) {
                    war.getFirstGuild().addKills(1);
                    war.getSecondGuild().addDeaths(1);
                } else {
                    war.getFirstGuild().addDeaths(1);
                    war.getSecondGuild().addKills(1);
                }
            }
        }
        final MessageType messageType = MessageType.KILLS;
        StringBuilder assistMessage = new StringBuilder(ChatUtil.fixColor("" + (userKillPacket.getAssistList().size() > 1 ? "§#80f9ffAsystowali: " : "§#80f9ffAsystowal: ") + "§#e3ff69"));
        if (userKillPacket.getAssistList().size() >= 1) {
            for (UUID uuid : userKillPacket.getAssistList().keySet()) {
                final User u = this.plugin.getUserManager().getUser(uuid);
                int points = userKillPacket.getAssistList().get(uuid);
                if (points == 0) {
                    points = 10;
                }
                assistMessage.append(ChatUtil.fixColor((u.getGuild() == null ? "§#e3ff69" : "&8[§#ff6eb6" + u.getGuild().getTag() + "&8] §#e3ff69") + u.getName() + " &8(§#68fcbc+§#06d436" + points + "&8)  "));

                u.addPoints(points);
                u.addAssist(1);
            }
        }
        for (final Player p : Bukkit.getOnlinePlayers()) {
            final User user = this.plugin.getUserManager().getUser(p.getUniqueId());
            if (!user.getChat().hasDisabled(messageType)) {
                p.sendMessage(ChatUtil.fixColor(userKillPacket.getBroadcastMessage()));
                if(userKillPacket.getAssistList().size() >= 1) {
                    p.sendMessage(ChatUtil.fixColor(assistMessage.toString()));
                }
            }
        }
        final Player p = kUser.asPlayer();
        if (p != null && p.isOnline()) {
            TitleUtil.sendTitle(p, 10, 100, 5, ChatUtil.fixColor("&8(->> §#ebd234ZA§#eb9e34BO§#eb7134JS§#eb5834TWO &8<<-)"), ChatUtil.fixColor("§#34ebe5Zabiles gracza §#ff4a8f" + victimUser.getName() + "&8 (§#66ffa6+§#00cf64" + userKillPacket.getAttackAddPoints() + "&8)"));
        }
        final Player v = victimUser.asPlayer();
        if(v != null && v.isOnline()){
            //TODO send health to packet
        }
    }

    @Override
    public void handle(UserPointsUpdatePacket userPointsUpdatePacket) {
        final User user = this.plugin.getUserManager().getUser(userPointsUpdatePacket.getUUID());
        if(user == null){
            ReportError.save("UserPointsUpdatePacket " + userPointsUpdatePacket.getUUID() + " jest nullem!");
            return;
        }
        user.setPoints(userPointsUpdatePacket.getUserPoints());
    }

    @Override
    public void handle(UserStatsUpdatePacket userStatsUpdatePacket) {
        final User user = this.plugin.getUserManager().getUser(userStatsUpdatePacket.getUserUUID());
        if (user == null) {
            ReportError.save("UserStatsUpdatePacket " + userStatsUpdatePacket.getUserUUID() + " jest nullem!");
            return;
        }
        user.setSpendMoney(userStatsUpdatePacket.getSpendMoney());
        user.setMinedStone(userStatsUpdatePacket.getMinedStone());
        user.setOpenedCase(userStatsUpdatePacket.getOpenedCase());
        user.setLastUpdate(System.currentTimeMillis());
        user.setLastJoin(userStatsUpdatePacket.getLastJoin());
        user.setTimePlay(userStatsUpdatePacket.getTimePlay());
    }

    @Override
    public void handle(GuildDestroyPacket guildDestroyPacket) {
        final Guild targetGuild = this.plugin.getGuildManager().getGuild(guildDestroyPacket.getTargetGuild());
        if (targetGuild != null) {
            for (UUID memberUUID : targetGuild.getMembers().keySet()) {
                final User user = this.plugin.getUserManager().getUser(memberUUID);
                user.setGuild(null);
                final Player p = user.asPlayer();
                if (p != null && p.isOnline()) {
                    this.plugin.getTagManager().updateBoard(p);
                }
            }
            this.plugin.getGuildManager().deleteGuild(targetGuild);
            String msg = GuildConfig.INSTANCE.MESSAGES_GUILDDESTROY;
            msg = msg.replace("{GUILD}", guildDestroyPacket.getAttackerGuild());
            msg = msg.replace("{GUILDVICTIM}", targetGuild.getTag());
            Bukkit.broadcastMessage(ChatUtil.fixColor(msg));
        }
    }

    @Override
    public void handle(GuildTakeLivePacket guildTakeLivePacket) {
        final Guild targetGuild = this.plugin.getGuildManager().getGuild(guildTakeLivePacket.getTargetGuild());
        if (targetGuild != null) {
            targetGuild.setHeartDurability(500);
            targetGuild.setGuildLastBreakHeart(System.currentTimeMillis());
            targetGuild.removeGuildLive(1);
            String msg = GuildConfig.INSTANCE.MESSAGES_GUILDTAKELIVE;
            msg = msg.replace("{GUILD}", guildTakeLivePacket.getAttackerGuild());
            msg = msg.replace("{GUILDVICTIM}", targetGuild.getTag());
            msg = msg.replace("{VICTIMLIVES}", String.valueOf(targetGuild.getGuildLives()));
            Bukkit.broadcastMessage(ChatUtil.fixColor(msg));
        }
    }

    @Override
    public void handle(GuildBreakHeartPacket guildBreakHeartPacket) {

    }

    @Override
    public void handle(GuildAllianceMessagePacket guildAllianceMessagePacket) {
        final Guild guild = this.plugin.getGuildManager().getGuild(guildAllianceMessagePacket.getGuildTag());
        if (guild != null) {
            guild.sendAllianceMessage(guildAllianceMessagePacket.getMessage());
        }
    }

    @Override
    public void handle(GuildMessagePacket guildMessagePacket) {
        final Guild guild = this.plugin.getGuildManager().getGuild(guildMessagePacket.getGuildTag());
        if (guild != null) {
            guild.sendGuildMessage(guildMessagePacket.getMessage());
        }
    }

    @Override
    public void handle(GuildExpirePacket guildExpirePacket) {
        final Guild g = this.plugin.getGuildManager().getGuild(guildExpirePacket.getTag());
        if (g == null) {
            ReportError.save("GuildExpirePacket " + guildExpirePacket.getTag() + " jest nullem!");
            return;
        }
        for (UUID memberUUID : g.getMembers().keySet()) {
            final User user = this.plugin.getUserManager().getUser(memberUUID);
            user.setGuild(null);
            final Player p = user.asPlayer();
            if (p != null && p.isOnline()) {
                this.plugin.getTagManager().updateBoard(p);
            }
        }
        this.plugin.getGuildManager().deleteGuild(g);
        Bukkit.broadcastMessage(ChatUtil.fixColor("&2&lGILDIE &8->> &7Gildia &8[&a" + g.getTag() + "&8]&7 wygasla! Jej koordynaty &8[&6&l" + g.getGuildRegion().getCenterX() + ";" + g.getGuildRegion().getCenterZ() + "&8]"));
    }

    @Override
    public void handle(GuildAddCoinsPacket guildAddCoinsPacket) {
        final Guild guild = this.plugin.getGuildManager().getGuild(guildAddCoinsPacket.getGuildTag());
        final User user = this.plugin.getUserManager().getUser(guildAddCoinsPacket.getExecutorName());
        guild.getGuildTreasury().addCoins(guildAddCoinsPacket.getCoinsAmount());
        user.removeCoins(guildAddCoinsPacket.getCoinsAmount());
        final Player p = user.asPlayer();
        if (p != null && p.isOnline()) {
            ChatUtil.sendMessage(p, "&2&lGILDIE &8->> &7Wplaciles do skarbca &6" + guildAddCoinsPacket.getCoinsAmount() + " &7monet!");
            if(p.getOpenInventory() != null){
                if(p.getOpenInventory().getItem(26) != null){
                    final ItemStack money = p.getOpenInventory().getItem(26);
                    final ItemMeta meta = money.getItemMeta();
                    if(meta != null && money.getType().equals(Material.GOLD_INGOT)) {
                        meta.setLore(Collections.singletonList(ChatUtil.fixColor("&8* &7Monety: " + guild.getGuildTreasury().getCoins())));
                        money.setItemMeta(meta);
                    }
                }
            }
        }
    }

    @Override
    public void handle(GuildBoughtRegenerationPacket guildBoughtRegenerationPacket) {
        final User user = this.plugin.getUserManager().getUser(guildBoughtRegenerationPacket.getExecutorName());
        final Player p = user.asPlayer();
        if (p != null && p.isOnline()) {
            ChatUtil.sendMessage(p, "&2&lGILDIE &8->> &aRozpoczynam regeneracje terenu...");
        }
        final Guild guild = this.plugin.getGuildManager().getGuild(guildBoughtRegenerationPacket.getGuildTag());
        if (guild == null) {
            ReportError.save("GuildBoughtRegenerationPacket " + guildBoughtRegenerationPacket.getGuildTag() + " jest nullem!");
            throw new RuntimeException("Nie ma gildii? ;/");
        }
        new RegenerationUtil(p, guild).start();
    }

    @Override
    public void handle(GuildRemoveCoinsPacket guildRemoveCoinsPacket) {
        final Guild guild = this.plugin.getGuildManager().getGuild(guildRemoveCoinsPacket.getGuildTag());
        final User user = this.plugin.getUserManager().getUser(guildRemoveCoinsPacket.getExecutorName());
        guild.getGuildTreasury().removeCoins(guildRemoveCoinsPacket.getCoinsAmount());
        final Player p = user.asPlayer();
        if (p != null && p.isOnline()) {
            ChatUtil.sendMessage(p, "&2&lGILDIE &8->> &7Wyplaciles do skarbca &6" + guildRemoveCoinsPacket.getCoinsAmount() + " &7monet!");
        }
    }

    @Override
    public void handle(GuildUpgradePacket guildUpgradePacket) {
    }


    @Override
    public void handle(GuildAddMemberPacket guildAddMemberPacket) {
        final Guild guild = this.plugin.getGuildManager().getGuild(guildAddMemberPacket.getGuildTag());
        final User user = this.plugin.getUserManager().getUser(guildAddMemberPacket.getGuildMemberUUID());
        guild.getInvites().invalidate(user.getUUID());
        this.plugin.getGuildManager().addMember(guild, new GuildMemberImpl(user.getUUID(), user.getName(), GuildPermission.BREAK, GuildPermission.PLACE));
        user.setGuild(guild);
        Bukkit.broadcastMessage(ChatUtil.fixColor("&2&lGILDIE &8->> &7Gracz &a" + user.getName() + " &7dolaczyl do gildii &8[&a" + guild.getTag() + "&8]"));
        final Player p = Bukkit.getPlayer(guildAddMemberPacket.getGuildMemberUUID());
        if (p != null && p.isOnline()) {
            this.plugin.getTagManager().updateBoard(p);
        }
    }

    @Override
    public void handle(GuildLeavePacket guildLeavePacket) {
        final User user = this.plugin.getUserManager().getUser(guildLeavePacket.getPlayerUUID());
        if (user == null) {
            ReportError.save("GuildLeavePacket not found for user " + guildLeavePacket.getPlayerName() + " (" + guildLeavePacket.getPlayerUUID() + ")");
            return;
        }
        final Player p = Bukkit.getPlayer(guildLeavePacket.getPlayerUUID());
        Bukkit.broadcastMessage(ChatUtil.fixColor("&2&lGILDIE &8->> &7Gracz &a" + user.getName() + "&7 opuscil gildie: &8[&a" + guildLeavePacket.getGuildTag() + "&8]"));
        final Guild userGuild = this.plugin.getGuildManager().getGuild(guildLeavePacket.getGuildTag());
        if (p != null && p.isOnline()) {
            this.plugin.getTagManager().updateBoard(p);
            final Guild g = this.plugin.getGuildManager().getGuild(p.getLocation());
            if (g != null && g.equals(userGuild)) {
                this.plugin.getTeleportManager().teleportToSpawn(user, p, true);
            }
        }
        if (userGuild != null) {
            this.plugin.getGuildManager().removeMember(userGuild, user);
        }
    }

    @Override
    public void handle(GuildAllyCreatePacket guildAllyCreatePacket) {
        final Guild g1 = this.plugin.getGuildManager().getGuild(guildAllyCreatePacket.getFirstGuildTag());
        final Guild g2 = this.plugin.getGuildManager().getGuild(guildAllyCreatePacket.getSecondGuildTag());
        this.plugin.getAllianceManager().addAlliance(guildAllyCreatePacket.getAllianceID(), g1, g2);
        //g1 zaakceptowala zapro g2
        for (Player onlineMember : g2.getOnlineMembersAsPlayers()) {
            ChatUtil.sendMessage(onlineMember, "&2&lGILDIE &8->> &7Gildia &e" + g1.getTag() + " &7zaakceptowala prosbe do sojuszu!");
            this.plugin.getTagManager().updateBoard(onlineMember);
        }
        for (Player onlineMember : g1.getOnlineMembersAsPlayers()) {
            ChatUtil.sendMessage(onlineMember, "&2&lGILDIE &8->> &7Twoja gildia nawiazala sojusz z gildia &e" + g2.getTag());
            this.plugin.getTagManager().updateBoard(onlineMember);
        }
    }

    @Override
    public void handle(GuildAllyRequestPacket guildAllyRequestPacket) {
        final Guild guildRequested = this.plugin.getGuildManager().getGuild(guildAllyRequestPacket.getGuildRequested());
        final Guild guildToAccept = this.plugin.getGuildManager().getGuild(guildAllyRequestPacket.getGuildToAccept());
        guildRequested.getAllianceInvites().put(guildToAccept.getTag(), System.currentTimeMillis());
        for (Player onlineMember : guildRequested.getOnlineMembersAsPlayers()) {
            ChatUtil.sendMessage(onlineMember, "&2&lGILDIE &8->> &7Otrzymaliscie zaproszenie do sojuszu od gildii: &e" + guildToAccept.getTag() + " &7napisz /sojusz &e" + guildToAccept.getTag() + " &7 aby zaakceptowac zaproszenie!");
        }
        final Player p = Bukkit.getPlayer(guildAllyRequestPacket.getRequestedPlayer());
        if (p != null && p.isOnline()) {
            ChatUtil.sendMessage(p, "&2&lGILDIE &8->> &8» &7Zaproszenie do sojuszu do gildii &e" + guildRequested.getTag() + " &7zostalo wyslane");
        }
    }

    @Override
    public void handle(GuildAllyRemovePacket guildAllyRemovePacket) {
        final Guild guild = this.plugin.getGuildManager().getGuild(guildAllyRemovePacket.getFirstGuildTag());
        final Guild guild2 = this.plugin.getGuildManager().getGuild(guildAllyRemovePacket.getSecondGuildTag());
        this.plugin.getAllianceManager().removeAlliance(guild, guild2);
        for (Player onlineMember : guild.getOnlineMembersAsPlayers()) {
            ChatUtil.sendMessage(onlineMember, "&2&lGILDIE &8->> &7Sojusz z gildia &8[&e" + guild2.getTag() + "&8] &7zostal zerwany!");
            this.plugin.getTagManager().updateBoard(onlineMember);
        }
        for (Player onlineMember : guild2.getOnlineMembersAsPlayers()) {
            ChatUtil.sendMessage(onlineMember, "&2&lGILDIE &8->> &7Sojusz z gildia &8[&e" + guild.getTag() + "&8] &7zostal zerwany!");
            this.plugin.getTagManager().updateBoard(onlineMember);
        }
    }

    @Override
    public void handle(GuildCreatePacket guildCreatePacket) {
        final Sector sector = this.plugin.getSectorManager().getSector(guildCreatePacket.getSectorName());
        if (sector == null) {
            ReportError.save("GuildCreatePacket sector null " + guildCreatePacket.getSectorName());
            return;
        }
        final User user = this.plugin.getUserManager().getUser(guildCreatePacket.getOwnerUUID());
        if (user == null) {
            ReportError.save("GuildCreatePacket user null " + guildCreatePacket.getOwnerUUID());
            return;
        }
        this.plugin.getGuildManager().createGuild(user, guildCreatePacket.getGuildTag(), guildCreatePacket.getGuildName(), sector, guildCreatePacket.getCenterX(), guildCreatePacket.getCenterZ());
    }

    @Override
    public void handle(GuildRemoveMemberPacket guildRemoveMemberPacket) {
        final Guild guild = this.plugin.getGuildManager().getGuild(guildRemoveMemberPacket.getGuildTag());
        final User user = this.plugin.getUserManager().getUser(guildRemoveMemberPacket.getKickedUUID());
        this.plugin.getGuildManager().removeMember(guild, user);
        final Player p = user.asPlayer();
        if (p != null && p.isOnline()) {
            this.plugin.getTagManager().updateBoard(p);
        }
    }

    @Override
    public void handle(GuildMemberUpdate guildMemberUpdate) {
        final Guild guild = this.plugin.getGuildManager().getGuild(guildMemberUpdate.getGuildTag());
        final GuildMember guildMember = guild.getMember(guildMemberUpdate.getMemberUUID());
        final GuildPermission permission = GuildPermission.valueOf(guildMemberUpdate.getRowName());
        if (guildMemberUpdate.getValue() == 1) {
            guildMember.removePermission(permission);
        } else {
            guildMember.addPermission(permission);
        }
    }

    @Override
    public void handle(WarUpdateValuesPacket warUpdateValuesPacket) {
        final War war = this.plugin.getWarManager().getWar(warUpdateValuesPacket.getFirstGuildTag(), warUpdateValuesPacket.getSecondGuildTag());
        if (war != null) {
            if (war.getSecondGuild().getTag().equalsIgnoreCase(warUpdateValuesPacket.getFirstGuildTag())) {
                war.getSecondGuild().setKills(warUpdateValuesPacket.getFirstGuildKills());
                war.getSecondGuild().setDeaths(warUpdateValuesPacket.getFirstGuildDeaths());
            } else {
                war.getFirstGuild().setKills(warUpdateValuesPacket.getSecondGuildKills());
                war.getFirstGuild().setDeaths(warUpdateValuesPacket.getFirstGuildDeaths());
            }
        }
    }

    @Override
    public void handle(AlliancePermissionChange alliancePermissionChange) {
        final Alliance alliance = this.plugin.getAllianceManager().getAlliance(alliancePermissionChange.getAllianceID());
        if (alliance != null) {
            final GuildAlliance guildAlliance = alliance.findGuildAlliance(alliancePermissionChange.getGuildTag());
            if (guildAlliance == null) {
                ReportError.save("No GuildAlliance with tag " + alliancePermissionChange.getGuildTag());
                throw new RuntimeException("No GuildAlliance with tag " + alliancePermissionChange.getGuildTag());
            }
            if (alliancePermissionChange.isPermissionStatus()) {
                guildAlliance.addPermission(AlliancePermission.WATER_PLACE);
            } else {
                guildAlliance.removePermission(AlliancePermission.WATER_PLACE);
            }
            final Player p = Bukkit.getPlayer(alliancePermissionChange.getPlayerID());
            if (p != null && p.isOnline()) {
                p.closeInventory();
                AllianceManageInventory.openAlliances(p, alliance, guildAlliance);
            }
        }
    }

    @Override
    public void handle(GuildLeaderChangePacket guildLeaderChangePacket) {
        final Guild guild = this.plugin.getGuildManager().getGuild(guildLeaderChangePacket.getGuildTag());
        guild.setOwnerUUID(guildLeaderChangePacket.getNewLeader());
        guild.setOwnerName(guildLeaderChangePacket.getNewLeaderName());
        final Player p = Bukkit.getPlayer(guildLeaderChangePacket.getOldLeader());
        final Player otherPlayer = Bukkit.getPlayer(guildLeaderChangePacket.getNewLeader());


        //ADD PERMISSIONS TO NEW LEADER  ACTION
        final GuildMember member = guild.getMember(guildLeaderChangePacket.getNewLeader());
        for(GuildPermission guildPermission : GuildPermission.values()) {
            if (!member.getPermissions().contains(guildPermission)){
                member.addPermission(guildPermission);
            }
        }
        //REMOVE PERMISSINS FROM OLD LEADER ACTION
        final GuildMember member1 = guild.getMember(guildLeaderChangePacket.getOldLeader());
        for(GuildPermission guildPermission : GuildPermission.values()){
            if(member1.getPermissions().contains(guildPermission)){
                member1.removePermission(guildPermission);
            }
        }
        if (p != null && p.isOnline()) {
            ChatUtil.sendMessage(p, " &8» &2Zaktualizowales role graczowi: &7" + plugin.getUserManager().getUser(guildLeaderChangePacket.getNewLeader()).getName() + " na: &eLIDER");
        }
        if (otherPlayer != null && otherPlayer.isOnline()) {
            ChatUtil.sendMessage(otherPlayer, "&2&lGILDIE &8->>  &7Zaktualizowano Ci role na &eLIDER&7 &2Gratulacje! &7od teraz jestes liderem gildii &8[&a" + guild.getTag() + "&8]");
        }
    }

    @Override
    public void handle(GuildRemovePacket guildRemovePacket) {
        final Guild guild = this.plugin.getGuildManager().getGuild(guildRemovePacket.getGuildTag());
        if (guild == null) {
            ReportError.save("GuildRemovePacket guild null " + guildRemovePacket.getGuildTag());
            return;
        }
        for (UUID memberUUID : guild.getMembers().keySet()) {
            final User user = this.plugin.getUserManager().getUser(memberUUID);
            user.setGuild(null);
            final Player p = user.asPlayer();
            if (p != null && p.isOnline()) {
                this.plugin.getTagManager().updateBoard(p);
            }
        }
        final Set<War> wars = this.plugin.getWarManager().getWars(guild);
        for (War war : wars) {
            if (war.getFirstGuild().getTag().equalsIgnoreCase(guild.getTag())) {
                war.getSecondGuild().asGuild().addWinWars(1);
            } else {
                war.getFirstGuild().asGuild().addWinWars(1);
            }
        }

        final Set<Alliance> alliances = this.plugin.getAllianceManager().getAlliances(guild);
        for (Alliance alliance : alliances) {
            this.plugin.getAllianceManager().deleteAlliance(alliance);
        }

        this.plugin.getGuildManager().deleteGuild(guild);
        Bukkit.broadcastMessage(ChatUtil.fixColor("&2&lGILDIE &8->>  &7Gildia &8[&a" + guild.getTag() + "&8] &ezostala usunieta przez &c" + guildRemovePacket.getExecutorName()));
    }

    @Override
    public void handle(GuildResizePacket guildResizePacket) {
        final Guild guild = this.plugin.getGuildManager().getGuild(guildResizePacket.getGuildTag());
        guild.getGuildRegion().setSize(guildResizePacket.getCurrentSize());
    }

    @Override
    public void handle(GuildSetBaseLocationPacket guildSetBaseLocationPacket) {
        final Guild guild = this.plugin.getGuildManager().getGuild(guildSetBaseLocationPacket.getGuildTag());
        guild.setHomeLocation(new Location(Bukkit.getWorld("world"), guildSetBaseLocationPacket.getGuildBaseX(), guildSetBaseLocationPacket.getGuildBaseY(), guildSetBaseLocationPacket.getGuildBaseZ()));
        final Player p = Bukkit.getPlayerExact(guildSetBaseLocationPacket.getExecutorName());
        if (p != null) {
            ChatUtil.sendMessage(p, "&2&lGILDIE &8->> &7Nowa lokalizacja bazy zostala ustawiona!");
        }
    }

    @Override
    public void handle(GuildSetPvPPacket guildSetPvPPacket) {
        final Guild guild = this.plugin.getGuildManager().getGuild(guildSetPvPPacket.getGuildTag());
        guild.setFriendlyFire(guildSetPvPPacket.isPvP());
        for (Player onlineMember : guild.getOnlineMembersAsPlayers()) {
            ChatUtil.sendMessage(onlineMember, "&2&lGILDIE &8->> &7PVP w gildii zostalo " + (guild.isFriendlyFire() ? "&ewlaczone" : "&cwylaczone") + " &7przez &a" + guildSetPvPPacket.getExecutorName());
        }
    }

    @Override
    public void handle(GuildInvitePacket guildInvitePacket) {
        final Guild guild = this.plugin.getGuildManager().getGuild(guildInvitePacket.getGuildTag());
        guild.getInvites().put(guildInvitePacket.getInvitedUUID(), guildInvitePacket.getExecutorName());
        final Player p = Bukkit.getPlayer(guildInvitePacket.getInvitedUUID());
        if (p != null) {
            ChatUtil.sendMessage(p, "&2&lGILDIE &8->> &7Zostałeś zaproszony do gildii &8[&a" + guild.getTag() + "&8] &7wpisz: &a/dolacz " + guild.getTag() + "&7 aby dolaczyc.");
        }
    }

    @Override
    public void handle(GuildKickPacket guilkdKickPacket) {
        final Guild guild = this.plugin.getGuildManager().getGuild(guilkdKickPacket.getGuildTag());
        final User user = this.plugin.getUserManager().getUser(guilkdKickPacket.getKickedUUID());
        final Player p = user.asPlayer();
        if (p != null && p.isOnline()) {
            this.plugin.getTagManager().updateBoard(p);
        }
        this.plugin.getGuildManager().removeMember(guild, user);
        String msg = GuildConfig.INSTANCE.MESSAGES_KICKFROMGUILD;
        msg = msg.replace("{PLAYER}", guilkdKickPacket.getKickedName());
        msg = msg.replace("{EXECUTOR}", guilkdKickPacket.getExecutorName());
        msg = msg.replace("{TAG}",guild.getTag());
        Bukkit.broadcastMessage(ChatUtil.fixColor(msg));
    }

    @Override
    public void handle(GuildAllySetPvPPacket guildAllySetPvPPacket) {
        final Guild guild = this.plugin.getGuildManager().getGuild(guildAllySetPvPPacket.getGuildTag());
        guild.setAllianceFriendlyFire(guildAllySetPvPPacket.isPvP());
        final Player p = Bukkit.getPlayer(guildAllySetPvPPacket.getExecutorName());
        if (p != null) {
            ChatUtil.sendMessage(p, " &8» &7PVP w sojuszu zostalo " + (guild.isAllianceFriendlyFire() ? "&ewlaczone" : "&cwylaczone") + " &7przez &e" + guildAllySetPvPPacket.getExecutorName());
        }
    }

    @Override
    public void handle(GuildLogPacket guildLogPacket) {
        final Guild guild = this.plugin.getGuildManager().getGuild(guildLogPacket.getGuildTag());
        if (guild == null) {
            ReportError.save("GuildLogPacket guild is null for " + guildLogPacket.getGuildTag() + " nick: " + guildLogPacket.getPlayerName() + " type: " + guildLogPacket.getLogType() + " desc: " + guildLogPacket.getLogDescription());
        }
        guild.registerLogAction(new GuildLogActionImpl(guild.getTag(), guildLogPacket.getPlayerName(), guildLogPacket.getLogDescription(), guildLogPacket.getLogTime(), guildLogPacket.getLogType()));
    }

    @Override
    public void handle(ClearInventoryPacket clearInventoryPacket) {
        final User user = this.plugin.getUserManager().getUser(clearInventoryPacket.getTargetUUID());
        final Player p = user.asPlayer();
        if (p != null) {
            p.getInventory().clear();
            ChatUtil.sendMessage(p, "&8|->> &7Twoje inventory zostalo wyczyszczone przez &a&n" + clearInventoryPacket.getExecutorName());
        }
    }


    @Override
    public void handle(RequestOnlineList requestOnlineList) {
        requestOnlineList.getOnlineMap().forEach((name, list) -> {
            final Sector sector = this.plugin.getSectorManager().getSector(name);
            if (sector == null) {
                throw new RuntimeException("Nie znaleziono sektora " + name + " (RequestOnlineList)");
            }
            sector.getOnlinePlayers().clear();
            for (String s : list) {
                sector.getOnlinePlayers().add(s);
            }
            this.plugin.getLogger().log(Level.INFO, "Zarejestrowano " + list.size() + " graczy na sektorze " + name);
        });
    }

    @Override
    public void handle(HelpopMessagePacket helpopMessagePacket) {
        final User senderUser = this.plugin.getUserManager().getUser(helpopMessagePacket.getPlayerName());
        if (senderUser == null) {
            ReportError.save("HelpopMessagePacket user null " + helpopMessagePacket.getPlayerName());
            return;
        }
        senderUser.getChat().updateHelpopDelay();
        for (Player p : Bukkit.getOnlinePlayers()) {
            final User user = this.plugin.getUserManager().getUser(p.getUniqueId()); //TODO trzymac liste z uprawnieniami optymalizacja
            if (user.getGroup().hasPermission(UserGroup.HELPER)) {
                p.sendMessage(ChatUtil.fixColor("&4[HelpOP] &6" + helpopMessagePacket.getPlayerName() + " &8->> &c" + helpopMessagePacket.getHelpopMesage()));
            }
        }
    }

    @Override
    public void handle(MessageToUserPacket messageToUserPacket) {
        final Player player = Bukkit.getPlayer(messageToUserPacket.getTarget());
        if (player != null && player.isOnline()) {
            ChatUtil.sendMessage(player, messageToUserPacket.getMessage());
        }
    }

    @Override
    public void handle(PlayerSpawnTeleportPacket playerSpawnTeleportPacket) {
        this.plugin.getTeleportManager().registerRequest(playerSpawnTeleportPacket.getUUID(), new TeleportableImpl("spawnLocation",
                Bukkit.getWorld("world").getSpawnLocation()
        ));
    }

    @Override
    public void handle(PlayerToPlayerTeleportPacket playerToPlayerTeleportPacket) {
        final User targetUser = this.plugin.getUserManager().getUser(playerToPlayerTeleportPacket.getTargetPlayer());
        final Player p = targetUser.asPlayer();
        if (p == null || !p.isOnline()) {
            final User reqUser = this.plugin.getUserManager().getUser(playerToPlayerTeleportPacket.getPlayerTeleport());
            reqUser.sendMessage("&cGracz do ktorego wyslales prosbe o teleportacje wyszedl.");
            return;
        }
        this.plugin.getTeleportManager().registerRequest(playerToPlayerTeleportPacket.getPlayerTeleport(), new TeleportableImpl("playerLocation", p.getLocation()));
    }

    @Override
    public void handle(PlayerTeleportPacket playerTeleportPacket) {
        this.plugin.getTeleportManager().registerRequest(playerTeleportPacket.getTeleportPlayer(), new TeleportableImpl("playerLocation",
                new Location(Bukkit.getWorld("world"), playerTeleportPacket.getLocX(), playerTeleportPacket.getLocY(), playerTeleportPacket.getLocZ())
        ));
    }

    @Override
    public void handle(PlayerRandomTeleportPacket playerRandomTeleportPacket) {
        final User targetUser = this.plugin.getUserManager().getUser(playerRandomTeleportPacket.getUUID());
        final Sector sector = this.plugin.getSectorManager().getCurrentSector();
        MinecraftServer.getServer().postToMainThread(() -> {
            this.plugin.getTeleportManager().registerRequest(targetUser.getUUID(), new TeleportableImpl("randomLocation",
                    TeleportUtil.randomLocation(sector.getMinX(), sector.getMaxX(), sector.getMinZ(), sector.getMaxZ())
            ));
        });
    }

    @Override
    public void handle(DeleteWarpPacket deleteWarpPacket) {
        this.plugin.getWarpManager().deleteWarp(deleteWarpPacket.getWarpName());
    }

    @Override
    public void handle(RegisterWarpPacket registerWarpPacket) {
        final Location location = new Location(Bukkit.getWorld("world"), registerWarpPacket.getLocX(), registerWarpPacket.getLocY(), registerWarpPacket.getLocZ());
        final Sector sector = this.plugin.getSectorManager().getSector(registerWarpPacket.getSectorName());
        final Warp warp = new WarpImpl(registerWarpPacket.getWarpName(), location, sector);
        this.plugin.getWarpManager().addWarp(warp);
    }

    @Override
    public void handle(DeleteHomePacket deleteHomePacket) {
        final User user = this.plugin.getUserManager().getUser(deleteHomePacket.getUserUUID());
        user.deleteHome(deleteHomePacket.getHomeName());
    }

    @Override
    public void handle(RegisterHomePacket registerHomePacket) {
        final User user = this.plugin.getUserManager().getUser(registerHomePacket.getUserUUID());
        user.registerHome(new HomeImpl(registerHomePacket.getHomeName(),
                new Location(Bukkit.getWorld("world"), registerHomePacket.getLocX(), registerHomePacket.getLocY(), registerHomePacket.getLocZ()),
                this.plugin.getSectorManager().getSector(registerHomePacket.getSectorName())
        ));
    }


    @Override
    public void handle(PrivateMessagePacket privateMessagePacket) {
        final User receiveUser = this.plugin.getUserManager().getUser(privateMessagePacket.getReceiverName());
        final User senderUser = this.plugin.getUserManager().getUser(privateMessagePacket.getSenderName());
        receiveUser.getChat().setLastConverser(senderUser);
        senderUser.getChat().setLastConverser(receiveUser);
        final Player p = receiveUser.asPlayer();
        if (p != null && p.isOnline()) {
            p.sendMessage(ChatUtil.fixColor("&c" + privateMessagePacket.getSenderName() + " -> " + p.getName() + " &8>> &c" + privateMessagePacket.getMessage()));
        }
    }


    @Override
    public void handle(GlobalGiveChestPacket globalGiveChestPacket) {
        final int amount = globalGiveChestPacket.getAmount();
        final ItemStack itemStack = plugin.getCaseManager().getCases().get("PREMIUMCASE").getCaseItem().clone();//TODO ENUM PREMIUMCASE
        itemStack.setAmount(amount);
        for(Player player : Bukkit.getOnlinePlayers()){
            if(player != null && player.isOnline()) {
                ItemUtil.giveItems(Collections.singletonList(itemStack), player);
            }
        }
    }

    @Override
    public void handle(GlobalEventPacket globalEventPacket) {
        String[] array = globalEventPacket.getEventString().split(";");
        final EventType type = EventType.getByInt(Integer.parseInt(array[1]));
        if(plugin.getEventManager().createGlobalEvent(array[0], type,array[2], Integer.parseInt(array[3]))) {
            Bukkit.broadcastMessage(ChatUtil.fixColor("&d&lEVENT &8->> &7Administrator &a&n" + array[0] + "&7 wlaczyl event &d&n" + EventUtil.getNameOfEvent(type) + "&8(&a" + (type == EventType.TURBODROP || type == EventType.TURBOEXP ? "+" : "x") + " &6" + array[3] + "&8)" + "&7 do &d&n" + TimeUtil.getDate(Long.parseLong(array[2]))));
        }
        if(type.equals(EventType.DROP)){
            plugin.getCoreConfig().EVENTSMANAGER_DROP_TIME = Long.parseLong(array[2]);
            plugin.getCoreConfig().EVENTSMANAGER_DROP_VALUE = Integer.parseInt(array[3]);

            plugin.getCoreConfig().setField("eventsmanager.drop.time", Long.parseLong(array[2]));
            plugin.getCoreConfig().setField("eventsmanager.drop.value", Integer.parseInt(array[3]));
            return;
        }
        if(type.equals(EventType.EXP)){
            plugin.getCoreConfig().EVENTSMANAGER_EXP_TIME = Long.parseLong(array[2]);
            plugin.getCoreConfig().EVENTSMANAGER_EXP_VALUE = Integer.parseInt(array[3]);

            plugin.getCoreConfig().setField("eventsmanager.exp.time", Long.parseLong(array[2]));
            plugin.getCoreConfig().setField("eventsmanager.exp.value", Integer.parseInt(array[3]));
            return;
        }
        if(type.equals(EventType.TURBODROP)){
            plugin.getCoreConfig().EVENTSMANAGER_TURBODROP_TIME = Long.parseLong(array[2]);
            plugin.getCoreConfig().EVENTSMANAGER_TURBODROP_VALUE = Integer.parseInt(array[3]);

            plugin.getCoreConfig().setField("eventsmanager.turbodrop.time", Long.parseLong(array[2]));
            plugin.getCoreConfig().setField("eventsmanager.turbodrop.value", Integer.parseInt(array[3]));
            return;
        }
        if(type.equals(EventType.TURBOEXP)){
            plugin.getCoreConfig().EVENTSMANAGER_TURBOEXP_TIME = Long.parseLong(array[2]);
            plugin.getCoreConfig().EVENTSMANAGER_TURBOEXP_VALUE = Integer.parseInt(array[3]);

            plugin.getCoreConfig().setField("eventsmanager.turboexp.time", Long.parseLong(array[2]));
            plugin.getCoreConfig().setField("eventsmanager.turboexp.value", Integer.parseInt(array[3]));
            return;
        }
    }

    @Override
    public void handle(GiveEventPacket giveEventPacket) {
        final User user = plugin.getUserManager().getUser(giveEventPacket.getTargetUUID());

        final String[] array = giveEventPacket.getEventString().split(";");
        final EventType type = EventType.getByInt(Integer.parseInt(array[0]));
        if (type == EventType.TURBODROP) {
            user.setTurboDropTime(Long.parseLong(array[1]));
        } else if (type == EventType.TURBOEXP) {
            user.setTurboExpTime(Long.parseLong(array[1]));
        }
    }

    @Override
    public void handle(AddCoinsPacket addCoinsPacket) {
        final User user = this.plugin.getUserManager().getUser(addCoinsPacket.getTargetUUID());
        user.addCoins(addCoinsPacket.getCoinsAmount());
        final Player targetPlayer = Bukkit.getPlayer(user.getUUID());
        if (targetPlayer != null && targetPlayer.isOnline()) {
            ChatUtil.sendMessage(targetPlayer, " &8» &7Administrator &e" + addCoinsPacket.getExecutorName() + " &7dodal do twojego portfela &e" + addCoinsPacket.getCoinsAmount() + " monet.");
        }
        final Player executorPlayer = Bukkit.getPlayer(addCoinsPacket.getExecutorName());
        if (executorPlayer != null && executorPlayer.isOnline()) {
            ChatUtil.sendMessage(executorPlayer, " &8» &7Gracz &e" + user.getName() + " &7otrzymal &a" + addCoinsPacket.getCoinsAmount() + " monet &8(" + (user.getSector().isOnline(user.getName()) ? "&aONLINE" : "&cOFFLINE"));
        }
    }

    @Override
    public void handle(TpaRequestPacket tpaRequestPacket) {
        final User targetUser = this.plugin.getUserManager().getUser(tpaRequestPacket.getTargetPlayer());
        final User requestUser = this.plugin.getUserManager().getUser(tpaRequestPacket.getRequestPlayer());
        targetUser.registerTpaRequest(requestUser.getName());
        final Player p = targetUser.asPlayer();
        if (p != null && p.isOnline()) {
            ChatUtil.sendMessage(p, " &8» &7Otrzymales prosbe o teleportacje od gracza &e" + requestUser.getName() + "&7, wpisz &e/tpaccept &7aby zaakceptowac.");
        }
    }

    @Override
    public void handle(TpaAcceptPacket tpaAcceptPacket) {
        final User teleportUser = this.plugin.getUserManager().getUser(tpaAcceptPacket.getTeleportPlayer());
        final User targetUser = this.plugin.getUserManager().getUser(tpaAcceptPacket.getTargetPlayer());
        this.plugin.getTeleportManager().teleportPlayer(teleportUser, targetUser,false);
        targetUser.removeTpaRequest(teleportUser.getName());
        final Player p = teleportUser.asPlayer();
        if (p != null && p.isOnline()) {
            ChatUtil.sendMessage(p, " &8» &7Twoja prosba o teleportacje zostala zaakceptowana!");
        }
    }

    @Override
    public void handle(TpaAcceptAllPacket tpaAcceptPacket) {
        final User targetUser = this.plugin.getUserManager().getUser(tpaAcceptPacket.getTargetPlayer());
        for (String teleportPlayer : tpaAcceptPacket.getTeleportPlayers()) {
            final User teleportUser = this.plugin.getUserManager().getUser(teleportPlayer);
            if (teleportUser == null) {
                ReportError.save("teleportUser " + teleportPlayer + " was null!");
                return;
            }
            this.plugin.getTeleportManager().teleportPlayer(teleportUser, targetUser, false);
            targetUser.removeTpaRequest(teleportUser.getName());
            final Player p = teleportUser.asPlayer();
            if (p != null && p.isOnline()) {
                ChatUtil.sendMessage(p, " &8» &7Twoja prosba o teleportacje zostala zaakceptowana!");
            }
        }
    }

    @Override
    public void handle(UserSetRankPacket userSetRankPacket) {
        final User targetUser = this.plugin.getUserManager().getUser(userSetRankPacket.getTargetUUID());
        final UserGroup group = UserGroup.valueOf(userSetRankPacket.getRankType());
        targetUser.setUserGroup(group);
        final Player p = targetUser.asPlayer();
        if (p != null && p.isOnline()) {
            ChatUtil.sendMessage(p, " &8» &7Twoja ranga zostala zmieniona na " + group.getGroupPrefix() + " &7przez &a" + userSetRankPacket.getExecutorName());
            plugin.getTagManager().updateBoard(p);
        }
    }

    @Override
    public void handle(UserStatUpdate userStatUpdate) {
        final User user = this.plugin.getUserManager().getUser(userStatUpdate.getUuid());
        try {
            final Field field = User.class.getField(userStatUpdate.getTableName());
            field.set(user, userStatUpdate.getNewValue());
        } catch (Exception e) {
            e.printStackTrace();//TODO custom exception + logger
        }
    }

    @Override
    public void handle(UserEatRefPacket userEatRefPacket) {
        this.plugin.getUserManager().getUser(userEatRefPacket.getPlayerUUID()).addEatRefil(1);
    }

    @Override
    public void handle(UserEatKoxPacket userEatKoxPacket) {
        this.plugin.getUserManager().getUser(userEatKoxPacket.getPlayerUUID()).addEatKox(1);
    }

    @Override
    public void handle(UserThrowPearlPacket userThrowPearlPacket) {
        this.plugin.getUserManager().getUser(userThrowPearlPacket.getPlayerUUID()).addThrowPearl(1);
    }

    @Override
    public void handle(ClearChatPacket clearChatPacket) {
        for (int i = 0; i < clearChatPacket.getCount(); i++) {
            Bukkit.broadcastMessage("");
        }
        final String message = ChatUtil.fixColor("&8(->>      &c&l§#1bc4a3E§#1bc4b3N§#1bc4c4I§#1bb9c4U§#1badc4M§#1b97c4C§#1b8cc4 CHAT       &8<<-)&8\n      &8* &7Chat zostal " + "&6wyczyszczony" + "\n&8(->>      &c&l§#1bc4a3E§#1bc4b3N§#1bc4c4I§#1bb9c4U§#1badc4M§#1b97c4C§#1b8cc4 CHAT       &8<<-)&8");
        Bukkit.broadcastMessage(message);
    }

    @Override
    public void handle(DiamondItemsPacket diamondItemsPacket) {
        plugin.getCoreConfig().setField("enablemanager.diamonditems",diamondItemsPacket.getTime());
        plugin.getCoreConfig().ENABLEMANAGER_DIAMONDITEMS = diamondItemsPacket.getTime();
    }

    @Override
    public void handle(GlobalTitlePacket globalTitlePacket) {
        for(Player player : Bukkit.getOnlinePlayers()){
            TitleUtil.sendTitle(player,20,100,30,ChatUtil.fixColor(globalTitlePacket.getTitle()),ChatUtil.fixColor(globalTitlePacket.getSubTitle()));
        }
    }

    @Override
    public void handle(SlowdownPacket slowDownPacket) {
        plugin.getCoreConfig().setField("chatmanager.slowdown", slowDownPacket.getSeconds());
        plugin.getCoreConfig().CHATMANAER_SLOWDOWN = slowDownPacket.getSeconds();
    }

    @Override
    public void handle(KitstatusPacket kitstatusPacket) {
        final Kit kit = plugin.getKitManager().getKit(kitstatusPacket.getKitKey());
        kit.setEnable(kitstatusPacket.isStatus());
        plugin.getKitConfig().setField("kits." + kitstatusPacket.getKitKey() + ".enable", kitstatusPacket.isStatus());
    }

    @Override
    public void handle(ServerStatusPacket serverStatusPacket) {
        final Sector sector = SectorPlugin.getInstance().getSectorManager().getSector(serverStatusPacket.getServerName());
        if (sector != null) {
            sector.setTps(serverStatusPacket.getCurrentTPS());
            sector.setLastUpdate(serverStatusPacket.getStatusTime());
            //plugin.getLogger().log(Level.INFO, "Zaktualizowano sektor " + sector.getSectorName());
        }
    }

    @Override
    public void handle(UserSectorUpdate userSectorUpdate) {
        final User user = this.plugin.getUserManager().getUser(userSectorUpdate.getUuid());
        final Sector sector = this.plugin.getSectorManager().getSector(userSectorUpdate.getSectorName());
        if (user != null && sector != null) {
            SectorPlugin.getInstance().getLogger().log(Level.INFO, " set sector " + sector.getSectorName());
            user.setUserSector(sector);
        }
    }

    @Override
    public void handle(ReloadConfigPacket reloadConfigPacket) {
        Config config = Config.valueOf(reloadConfigPacket.getConfig());
        plugin.getConfigManager().getConfig(config).reloadConfig();
        plugin.getConfigManager().getConfig(config).loadConfig();
    }

    @Override
    public void handle(ToggleManagerPacket toggleManagerPacket) {
        final CoreConfig coreConfig = plugin.getCoreConfig();
        coreConfig.setField(toggleManagerPacket.getPath(),toggleManagerPacket.isStatus());
        switch (toggleManagerPacket.getPath().replaceAll("togglemanager.","")){
            case "pearls":{
                coreConfig.TOGGLEMANAGER_PEARLS = toggleManagerPacket.isStatus();
                break;
            }
            case "pearlsonair":{
                coreConfig.TOGGLEMANAGER_PEARLSONAIR = toggleManagerPacket.isStatus();
                break;
            }
            case "punch":{
                coreConfig.TOGGLEMANAGER_PUNCH = toggleManagerPacket.isStatus();
                break;
            }
            case "knockback":{
                coreConfig.TOGGLEMANAGER_KNOCKBACK = toggleManagerPacket.isStatus();
                break;
            }
            case "pearlcooldown.status":{
                coreConfig.TOGGLEMANAGER_PEARLCOOLDOWN_STATUS = toggleManagerPacket.isStatus();
                break;
            }
            default: {
                plugin.getLOGGER().log(Level.WARNING, "ToggleManagerPacket doesnt found boolean to toggle ! (828)");
                break;
            }
        }

    }

    @Override
    public void handle(UserJoinedSectorPacket userJoinedSectorPacket) {
        final Sector sector = this.plugin.getSectorManager().getSector(userJoinedSectorPacket.getSectorName());
        if (sector != null) {
            sector.getOnlinePlayers().add(userJoinedSectorPacket.getUserName());
        }
        final User user = this.plugin.getUserManager().getUser(userJoinedSectorPacket.getUserName());
        if (user != null) {
            user.setUserSector(sector);
        }
    }

    @Override
    public void handle(UserLeftSectorPacket userLeftSectorPacket) {
        final Sector sector = this.plugin.getSectorManager().getSector(userLeftSectorPacket.getSectorName());
        if (sector != null) {
            sector.getOnlinePlayers().remove(userLeftSectorPacket.getUserName());
        }
    }

    @Override
    public void handle(UserChangeSectorPacket userChangeSectorPacket) {
        /*final User u = this.plugin.getUserManager().getUser(userChangeSectorPacket.getUuid());
        if(u == null) {
            return;
        }
        final EntityPlayer entityPlayer = u.asEntityPlayer();
        if (entityPlayer != null) {
            final NBTTagCompound nbtTagCompound = SectorPlugin.getInstance().getNbtManager().asNBT(u.getNbtString());
            u.setLastUpdate(System.currentTimeMillis());
            MinecraftServer.getServer().postToMainThread(() -> {
                SectorPlugin.getInstance().getNbtManager().applyToPlayer(u, nbtTagCompound);
            });
        }*/
    }

    @Override
    public void handle(SectorRegisterPacket sectorRegisterPacket) {
        final Sector sector = this.plugin.getSectorManager().getSector(sectorRegisterPacket.getSectorName());
        if (sector == null) {
            ReportError.save("Nie znaleziono sektora " + sectorRegisterPacket.getSectorName() + " (SectorRegisterPacket)");
            throw new RuntimeException("Nie znaleziono sektora " + sectorRegisterPacket.getSectorName() + " (SectorRegisterPacket)");
        }
        sector.getOnlinePlayers().clear();
    }

    @Override
    public void handle(SectorDisablePacket sectorDisablePacket) {
        final Sector sector = this.plugin.getSectorManager().getSector(sectorDisablePacket.getSectorName());
        if (sector == null) {
            ReportError.save("Nie znaleziono sektora " + sectorDisablePacket.getSectorName() + " (SectorRegisterPacket)");
            throw new RuntimeException("Nie znaleziono sektora " + sectorDisablePacket.getSectorName() + " (SectorDisablePacket)");
        }
        sector.getOnlinePlayers().clear();
    }

    @Override
    public void handle(PlayerSendTitlePacket playerSendTitlePacket) {
        final Player player = Bukkit.getPlayer(playerSendTitlePacket.getTarget());

        if (player != null && player.isOnline()) {
            TitleUtil.sendTitle(player, 20, 100, 30, ChatUtil.fixColor(playerSendTitlePacket.getTitle()), ChatUtil.fixColor(playerSendTitlePacket.getSubtitle()));
        }
    }

    @Override
    public void handle(UserSpawnTeleport userSpawnTeleport) {
        final User user = this.plugin.getUserManager().getUser(userSpawnTeleport.getUserName());
        if (user != null) {
            user.setUserLocation(Bukkit.getWorld("world").getSpawnLocation());
            this.plugin.getLogger().log(Level.INFO, "Przeteleportowano gracza " + user.getName() + " na spawn location!");
        }
    }

    @Override
    public void handle(GuildAlertPacket guildAlertPacket) {
        final Guild guild = this.plugin.getGuildManager().getGuild(guildAlertPacket.getGuildTag());
        for (GuildMember member : guild.getOnlineMembers()) {
            final Player player = Bukkit.getPlayer(member.getUUID());
            if (player != null && player.isOnline()) {
                TitleUtil.sendTitle(player, 5, 20, 5, ChatUtil.fixColor("&8->> &a&nGUILD&7 &a&nALERT&8 <<-"), ChatUtil.fixColor(guildAlertPacket.getMessage()));
            }
        }
    }

    @Override
    public void handle(GuildWarStartPacket guildWarStartPacket) {
        final Guild g = this.plugin.getGuildManager().getGuild(guildWarStartPacket.getGuildTag());
        if (g != null) {
            g.sendGuildMessage("&2&lGILDIE &8->> &7Wypowiedzieliscie wojne gildii &8[&c" + guildWarStartPacket.getSecondGuildTag() + "&8]");
        }
        final Guild o = this.plugin.getGuildManager().getGuild(guildWarStartPacket.getSecondGuildTag());
        if (o != null) {
            o.sendGuildMessage("&2&lGILDIE &8->> &7Wypowiedzieliscie wojne gildii &8[&c" + guildWarStartPacket.getGuildTag() + "&8]");
        }
        Bukkit.broadcastMessage("&2&lGILDIE &8->> &7Gildia &8[&c" + guildWarStartPacket.getGuildTag() + "&8] &7wypowiedziala wojne gildii &8[&c" + guildWarStartPacket.getSecondGuildTag() + "&8]");

        if (g != null && o != null) {
            this.plugin.getWarManager().createWar(g, o);
        }
    }

    @Override
    public void handle(GuildResetCollectPacket packet) {
        final Guild guild = plugin.getGuildManager().getGuild(packet.getGuildTag());
        if (guild != null) {
            guild.setCollectMaterial(Material.AIR);
            guild.setCollectAmount(0);
            guild.setCollectedAmount(0);
        }
    }

    @Override
    public void handle(GuildCollectAddPacket packet) {
        final Guild guild = plugin.getGuildManager().getGuild(packet.getGuildTag());
        if(guild != null) {
            guild.addCollectedAmount(packet.getAmount());
            for (GuildMember member : guild.getOnlineMembers()) {
                final Player player = Bukkit.getPlayer(member.getUUID());
                if(player != null){
                    ChatUtil.sendMessage(player,"&d&lZBIORKA &8->> &7Gracz &6" + packet.getExecutor() + " &7wplacil na zbiorke przedmiot w ilosci: &6" + packet.getAmount());
                }
            }
        }
    }

    @Override
    public void handle(GuildCreateCollectPacket packet) {
        final Guild guild = plugin.getGuildManager().getGuild(packet.getGuildTag());
        if(guild != null){

            guild.setCollectAmount(packet.getAmount());
            guild.setCollectedAmount(0);
            guild.setCollectMaterial(Material.getMaterial(packet.getMaterial()));

            for (GuildMember member : guild.getOnlineMembers()) {
                final Player player = Bukkit.getPlayer(member.getUUID());
                if(player != null){
                    ChatUtil.sendMessage(player,"&d&lZBIORKA &8->> &7Gracz &6" + packet.getExecutor() + " &7stworzyl nowa zbiorke na &a" + ItemUtil.getPolishMaterial(Material.getMaterial(packet.getMaterial())) + "&8x&2" + packet.getAmount());
                }
            }
        }
    }

    @Override
    public void handle(GuildPresetPermissionUpdatePacket packet) {
        final Guild guild = plugin.getGuildManager().getGuild(packet.getGuild());
        if(guild != null){
            final List<GuildPermission> permissions = guild.getPreset(packet.getPresetName());

            if(permissions.contains(packet.getPermission())){
                guild.removeFromPreset(packet.getPermission(),packet.getPresetName());
            }else{
                guild.addToPreset(packet.getPermission(),packet.getPresetName());
            }
            final Player player = Bukkit.getPlayer(packet.getExecutor());

            if(player != null && player.isOnline()){
                //TODO OPEN INV
                player.closeInventory();
                MemberPermissionInventory.openPresetMenu(player,guild,packet.getPresetName());
            }
            if(plugin.getSectorManager().getCurrentSector().getSectorName().equals(packet.getSector())){
                guild.insert(true);
            }
        }
    }

    @Override
    public void handle(GuildPresetRenamePacket packet) {
        final Guild guild = plugin.getGuildManager().getGuild(packet.getGuild());
        if(guild != null){
            guild.renamePreset(packet.getPresetName(),packet.getNewPresetName());

            final Player player = Bukkit.getPlayer(packet.getExecutor());
            if(player != null && player.isOnline()){
                //TODO OPEN INV
                player.closeInventory();
                MemberPermissionInventory.openPresetMenu(player,guild,packet.getNewPresetName());
            }
            if(plugin.getSectorManager().getCurrentSector().getSectorName().equals(packet.getSector())){
                guild.insert(true);
            }
        }
    }

    @Override
    public void handle(SetTntHourPacket packet) {
        if(packet.getHourType().equalsIgnoreCase("enable")) {
            plugin.getCoreConfig().TNTMANAGER_FROM = packet.getHour();

            plugin.getCoreConfig().getConfig().set("tntmanager.from", packet.getHour());
            plugin.getCoreConfig().loadConfig();
            plugin.getCoreConfig().reloadConfig();

            plugin.getTntManager().enableHour = packet.getHour();
        }else if(packet.getHourType().equalsIgnoreCase("disable")) {
            plugin.getCoreConfig().TNTMANAGER_TO = packet.getHour();

            plugin.getCoreConfig().getConfig().set("tntmanager.to", packet.getHour());
            plugin.getCoreConfig().loadConfig();
            plugin.getCoreConfig().reloadConfig();

            plugin.getTntManager().disableHour = packet.getHour();
        }
    }

    @Override
    public void handle(ChatStatusTogglePacket packet) {
        plugin.getChatManager().updateStatus();
    }

    @Override
    public void handle(ToggleTntStatusPacket packet) {
        TntManager.enable = packet.isEnable();
    }

    @Override
    public void handle(TopPacket topPacket) {
        this.plugin.getTopManager().replaceTop(topPacket.getTopType(), topPacket.getTopList());
    }

    @Override
    public void handle(EnableEnchantPacket packet) {
        plugin.getCoreConfig().setField("enablemanager.enchant",packet.getTime());
        plugin.getCoreConfig().ENABLEMANAGER_ENCHANT = packet.getTime();
    }

    @Override
    public void handle(BossBarSendPacket packet) {
        String text= packet.getText();
        if(text.equalsIgnoreCase( "stop")){
            BossBarCommand.enable = false;
            BossBarCommand.packet.setOperation(BarOperation.REMOVE);
            for(Player player : Bukkit.getOnlinePlayers()){
                PlayerConnection conn = ((CraftPlayer) player).getHandle().playerConnection;
                conn.sendPacket(new PacketPlayOutCustomPayload("BP|UpdateBossInfo", BossBarCommand.packet.serialize()));
            }

            return;
        }
        BossBarCommand.packet.setOperation(BarOperation.REMOVE);
        for(Player player : Bukkit.getOnlinePlayers()){
            PlayerConnection conn = ((CraftPlayer) player).getHandle().playerConnection;
            conn.sendPacket(new PacketPlayOutCustomPayload("BP|UpdateBossInfo", BossBarCommand.packet.serialize()));
        }
        BossBarCommand.packet = BossBarBuilder.add(BossBarCommand.packetUUID)
                .style(BarStyle.SOLID)
                .color(BarColor.BLUE)
                .title(TextComponent.fromLegacyText(ChatUtil.fixColor(text)))
                .progress(1.0f)
                .buildPacket();
        BossBarCommand.packet.setOperation(BarOperation.ADD);
        BossBarCommand.enable = true;
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerConnection conn = ((CraftPlayer) player).getHandle().playerConnection;
            conn.sendPacket(new PacketPlayOutCustomPayload("BP|UpdateBossInfo", BossBarCommand.packet.serialize()));
        }
    }

    @Override
    public void handle(UserDataAccepted packet) {
        SectorPlugin.getInstance().getSectorManager().handleSectorConnect(packet.getUuid());
    }

    @Override
    public void handle(ProxyStatusPacket packet) {
        final Proxy proxy = ProxyManager.getProxy(packet.getProxyName());
        if (proxy == null) {
            return;
        }
        proxy.tickUpdate();
        proxy.setCpuUsage(packet.getCpuUsage());
    }

    @Override
    public void handle(GuildPresetApplyPacket packet) {
        final Guild guild = plugin.getGuildManager().getGuild(packet.getGuildTag());
        if(guild != null){
            final GuildMember member = guild.getMember(packet.getApplyUser());
            if(member != null) {
                final List<GuildPermission> permissions = guild.getPreset(packet.getPresetName());
                if (permissions != null) {
                    member.removeAllPermissions();
                    permissions.forEach(permission -> {
                        if (!member.hasPermission(permission)) {
                            member.addPermission(permission);
                        }
                    });
                }
            }
        }
    }

    @Override
    public void handle(GuildRemoveAllPermissionsPacket packet) {
        final Guild guild = plugin.getGuildManager().getGuild(packet.getGuildTag());
        if (guild != null) {
            final GuildMember member = guild.getMember(packet.getMember());
            if (member != null) {
                member.removeAllPermissions();
            }
        }
    }

    @Override
    public void handle(GuildAddAllPermissionsPacket packet) {
        final Guild guild = plugin.getGuildManager().getGuild(packet.getGuildTag());
        if (guild != null) {
            final GuildMember member = guild.getMember(packet.getMember());
            if (member != null) {
                member.addAllPermissions();
            }
        }
    }

    @Override
    public void handle(TeleportHerePacket packet) {
        final User teleportUser = this.plugin.getUserManager().getUser(packet.getTeleportUser());
        final User targetUser = this.plugin.getUserManager().getUser(packet.getTargetUser());
        this.plugin.getTeleportManager().teleportPlayer(teleportUser, targetUser,true);
    }
}