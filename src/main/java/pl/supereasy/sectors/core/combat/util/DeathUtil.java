package pl.supereasy.sectors.core.combat.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.impl.user.UserKillPacket;
import pl.supereasy.sectors.core.combat.data.Combat;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.MathUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DeathUtil {


    public static boolean isLastKill(final User u, final Player p) {
        return u.getLastKillTime() > System.currentTimeMillis() && u.getLastKill().equalsIgnoreCase(p.getName());
    }
    public static boolean isAssist(final Combat deathPerson){
        return deathPerson.getAssists().size() > 1;
    }

    public static int calculateAddRanking(User killer, User victim){
        int add = (int)(64.0 + (killer.getPoints() - (victim != null ? victim.getPoints() : 0)) * -0.25);
        if(add <= 0){
            add = 1;
        }
        return add;
    }
    public static int calculateAssistRanking(User assist, User victim,double percent){
        int add = (int)(12.0 + (assist.getPoints() - (victim != null ? victim.getPoints() : 0)) * -0.25);
        if(add <= 0){
            add = 0;
        }
        return (int) (add + add * (percent/25));
    }
    public static int calculateRemoveRanking(User killer, User victim){
        return calculateAddRanking(killer, victim) / 4 * 3;
    }

    public static String deathMessage(final int plus, final int minus, final Player victim, final Player killer) {
        final Guild vGuild = SectorPlugin.getInstance().getGuildManager().getGuild(victim.getUniqueId());
        final Guild kGuild = SectorPlugin.getInstance().getGuildManager().getGuild(killer.getUniqueId());

        return ChatUtil.fixColor("§#80f9ffGracz " + (vGuild == null ? "&c" : "&8[§#ff6eb6" + vGuild.getTag() + "&8] §#ff5c67") + victim.getDisplayName() + " &8(§#96000a-§#db000f" + minus + "&8) §#80f9ffzostal zabity przez " + (kGuild == null ? "&c" : "&8[§#ff6eb6" + kGuild.getTag() + "&8] §#ff5c67") + killer.getDisplayName() + " &8(§#68fcbc+§#06d436" + plus + "&8)");
    }


    public static Map<UUID, Integer> getAssistList(Combat victimCombat, Player killer) {
        final HashMap<UUID, Integer> assists = new HashMap<>();
        final int dmg = victimCombat.countDamage();
        for (Player player : victimCombat.getAssists().keySet()) {
            if (System.currentTimeMillis() - victimCombat.getAssists().get(player) < 20000L) {
                final double procent = MathUtil.round((double) victimCombat.getDamage().get(player) / dmg * 100.0, 1);
                if (procent >= 15) {
                    if (!player.equals(killer)) {
                        assists.put(player.getUniqueId(), calculateAssistRanking(SectorPlugin.getInstance().getUserManager().getUser(player.getDisplayName()), SectorPlugin.getInstance().getUserManager().getUser(victimCombat.getPlayer().getDisplayName()), procent));
                    }
                }
            }
        }
        return assists;
    }


    public static void assists(Combat victimCombat, Player killer) {
        final HashMap<Player, Integer> assists = new HashMap<>();
        final int dmg = victimCombat.countDamage();
        for (Player player : victimCombat.getAssists().keySet()) {
            if (System.currentTimeMillis() - victimCombat.getAssists().get(player) < 20000L) {
                final double procent = MathUtil.round((double) victimCombat.getDamage().get(player) / dmg * 100.0, 1);
                if (procent >= 15) {
                    if (!player.equals(killer)) {
                        assists.put(player, calculateAssistRanking(SectorPlugin.getInstance().getUserManager().getUser(player.getDisplayName()), SectorPlugin.getInstance().getUserManager().getUser(victimCombat.getPlayer().getDisplayName()), (int) procent));
                    }
                }
            }
        }
        if(assists.size() >= 1) {
            StringBuilder assistMessage = new StringBuilder(ChatUtil.fixColor("" + (assists.size() > 1 ? "§#80f9ffAsystowali: " : "§#80f9ffAsystowal: ") + "§#e3ff69"));
            for (Player player : assists.keySet()) {
                final Guild aGuild = SectorPlugin.getInstance().getGuildManager().getGuild(player.getUniqueId());
                assistMessage.append(ChatUtil.fixColor((aGuild == null ? "§#e3ff69" : "&8[§#ff6eb6" + aGuild.getTag() + "&8] §#e3ff69") + player.getDisplayName() + " &8(§#68fcbc+§#06d436" + assists.get(player) + "&8)  "));
                final User user = SectorPlugin.getInstance().getUserManager().getUser(player.getUniqueId());
                user.addPoints(assists.get(player));
                user.addAssist(1);
            }
            Bukkit.broadcastMessage(assistMessage.toString());
        }
    }
    public static void remove(final Combat combat) {
        if (combat == null) {
            return;
        }
        combat.setLastAttackPlayer(null);
        combat.setLastAttackTime(0L);
        combat.getDamage().clear();
        combat.getAssists().clear();
    }
}
