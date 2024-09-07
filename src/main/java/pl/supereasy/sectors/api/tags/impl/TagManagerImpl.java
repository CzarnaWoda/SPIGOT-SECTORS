package pl.supereasy.sectors.api.tags.impl;

import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_8_R3.Scoreboard;
import net.minecraft.server.v1_8_R3.ScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.tags.api.TagManager;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.enums.RelationType;
import pl.supereasy.sectors.util.ChatUtil;

public class
TagManagerImpl implements TagManager {

    private final SectorPlugin plugin;
    private final Scoreboard scoreboard;

    public TagManagerImpl(SectorPlugin plugin) {
        this.plugin = plugin;
        this.scoreboard = new Scoreboard();
    }

    @Override
    public void createBoard(Player p) {
        try {
            final ScoreboardTeam hasTeam = scoreboard.getTeam(p.getName());
            if(hasTeam != null && scoreboard.getPlayerTeam(p.getName()) == null){
                scoreboard.removeTeam(hasTeam);
            }
            if(hasTeam == null && scoreboard.getPlayerTeam(p.getName()) != null){
                ScoreboardTeam team = scoreboard.getPlayerTeam(p.getName());
                scoreboard.removeTeam(team);
            }
            ScoreboardTeam team = scoreboard.getPlayerTeam(p.getName());
            if(team == null) {
                team = scoreboard.createTeam(p.getName());
            }
            //ScoreboardTeam team = scoreboard.getPlayerTeam(p.getName()) == null ? scoreboard.createTeam(p.getName()) : scoreboard.getPlayerTeam(p.getName());
            scoreboard.addPlayerToTeam(p.getName(), team.getName());
            team.setPrefix("");
            team.setDisplayName("");
            team.setSuffix("");
            PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam(team, 0);
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
            for (Player pp : Bukkit.getServer().getOnlinePlayers()) {
                if (pp != p) {
                    ((CraftPlayer) pp).getHandle().playerConnection.sendPacket(packet);
                }
            }
            for (Player pp : Bukkit.getServer().getOnlinePlayers()) {
                if (pp != p) {
                    ScoreboardTeam t = scoreboard.getTeam(pp.getName());
                    PacketPlayOutScoreboardTeam packetShow = new PacketPlayOutScoreboardTeam((t != null ? t : scoreboard.createTeam(pp.getName())), 0);
                    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetShow);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateBoard(Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        updateOthersFor(p, online);
                        updateOthersFor(online, p);
                    }
                }
        );
    }

    @Override
    public void deleteBoard(Player p) {
        try {
            ScoreboardTeam team = null;
            if (scoreboard.getPlayerTeam(Bukkit.getPlayer(p.getName()).getName()) == null) {
                return;
            }
            team = scoreboard.getPlayerTeam(p.getName());
            scoreboard.removePlayerFromTeam(p.getName(), team);
            PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam(team, 1);
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
            for (Player pp : Bukkit.getServer().getOnlinePlayers()) {
                if (pp != p) {
                    ((CraftPlayer) pp).getHandle().playerConnection.sendPacket(packet);
                }
            }
            for (Player pp : Bukkit.getServer().getOnlinePlayers()) {
                if (pp != p) {
                    ScoreboardTeam t = scoreboard.getTeam(pp.getName());
                    PacketPlayOutScoreboardTeam packetHide = new PacketPlayOutScoreboardTeam(t, 1);
                    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetHide);
                }
            }
            scoreboard.removeTeam(team);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateOthersFor(Player send, Player p) {
        ScoreboardTeam team = scoreboard.getPlayerTeam(p.getName());
        if(team == null){
            createBoard(p);
            team = scoreboard.getPlayerTeam(p.getName());
        }
        User get = plugin.getUserManager().getUser(p.getUniqueId());
        User s = plugin.getUserManager().getUser(send.getUniqueId());
        team.setPrefix(getValidPrefix(get, s));
        team.setSuffix(getValidSuffix(get));
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam(team, 2);
        ((CraftPlayer) send).getHandle().playerConnection.sendPacket(packet);
    }
    private String getValidPrefix(User get, User send) {
        RelationType type = this.plugin.getGuildManager().getRelation(get, send);
        String tag = "";
        Guild g = get.getGuild();
        if (g != null) {
            tag = ChatColor.DARK_GRAY + "[" + type.getColor() + (g.getTag()) + ChatColor.DARK_GRAY + "] " + ChatColor.WHITE;
        }
        return tag;
    }
    private String getValidSuffix(User get){
        if(get.getGroup().name().equalsIgnoreCase(UserGroup.VIP.name())){
            return  ChatUtil.fixColor(" &8| &6&lVIP");
        }
        if(get.getGroup().name().equalsIgnoreCase(UserGroup.SVIP.name())){
            return  ChatUtil.fixColor(" &8| &6&lSVIP");
        }
        if(get.getGroup().name().equalsIgnoreCase(UserGroup.ADMIN.name())){
            return  ChatUtil.fixColor(" &8| &cA");
        }
        if(get.getGroup().name().equalsIgnoreCase(UserGroup.WLASCICIEL.name())){
            return  ChatUtil.fixColor(" &8| &4ROOT");
        }
        if(get.getGroup().name().equalsIgnoreCase(UserGroup.HELPER.name())){
            return  ChatUtil.fixColor(" &8| &bH");
        }
        if(get.getGroup().name().equalsIgnoreCase(UserGroup.MODERATOR.name())){
            return  ChatUtil.fixColor(" &8| &aM");
        }
        if(get.getGroup().name().equalsIgnoreCase(UserGroup.HEADADMIN.name())){
            return  ChatUtil.fixColor(" &8| &4HA");
        }
        return "";
    }
}