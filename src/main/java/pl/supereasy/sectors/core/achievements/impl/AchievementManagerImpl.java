package pl.supereasy.sectors.core.achievements.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.chat.BroadcastChatMessage;
import pl.supereasy.sectors.api.packets.impl.chat.GlobalChatMessage;
import pl.supereasy.sectors.core.achievements.data.Achievement;
import pl.supereasy.sectors.core.achievements.enums.AchievementType;
import pl.supereasy.sectors.core.achievements.managers.AchievementManager;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AchievementManagerImpl implements AchievementManager {

    private final SectorPlugin  plugin;
    private final List<Achievement> achievements;

    public AchievementManagerImpl(SectorPlugin plugin){
        this.plugin = plugin;
        this.achievements = new ArrayList<>();
    }
    @Override
    public void createAchievement(Achievement achievement) {
        getAchievements().add(achievement);
    }

    @Override
    public Achievement getAchievement(String key) {
        for(Achievement a : achievements){
            if(a.getKey().equalsIgnoreCase(key)){
                return a;
            }
        }
        return null;
    }

    @Override
    public List<Achievement> getAchievements() {
        return achievements;
    }

    @Override
    public List<Achievement> getAchievements(AchievementType type) {
        List<Achievement> ach =  new ArrayList<>();
        for(Achievement a : achievements){
            if(a.getAchievementType().equals(type)){
                ach.add(a);
            }
        }
        return ach;
    }

    @Override
    public void complete(User user, Achievement achievement) {
        if(!isComplete(user,achievement)){
            final Player player = Bukkit.getPlayer(user.getUUID());
            if(player != null){
                user.getAchievements().add(achievement.getKey());
                if(achievement.isReward()) {
                    ItemUtil.giveItems(achievement.getRewards(), player);
                }
                if(achievement.isMoneyReward()){
                    user.addCoins(achievement.getMoneyReward());
                }
                final String message = ChatUtil.fixColor( "&dOSIAGNIECIA &8->> &7Gracz &a&n" + player.getDisplayName() + "&7 zdobyl osiagniecie &c&n" + achievement.getAchievementName());

                final Packet packet = new BroadcastChatMessage(message);

                plugin.getSectorClient().sendGlobalPacket(packet);
            }
        }
    }

    @Override
    public boolean isComplete(User user, Achievement achievement) {
        return user.getAchievements().contains(achievement.getKey());
    }
}
