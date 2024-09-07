package pl.supereasy.sectors.core.achievements.data;

import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.core.achievements.enums.AchievementType;

import java.util.List;

public class Achievement {


    private final String key;
    private final String achievementName;
    private final int amount;
    private final AchievementType achievementType;
    private final List<ItemStack> rewards;
    private final int moneyReward;


    public Achievement(String key, int amount, AchievementType achievementType, List<ItemStack> rewards, int moneyReward, String achievementName){
        this.key = key;
        this.amount = amount;
        this.achievementType = achievementType;
        this.rewards = rewards;
        this.moneyReward = moneyReward;
        this.achievementName = achievementName;
    }

    public int getAmount() {
        return amount;
    }

    public List<ItemStack> getRewards() {
        return rewards;
    }

    public AchievementType getAchievementType() {
        return achievementType;
    }

    public int getMoneyReward() {
        return moneyReward;
    }

    public String getKey() {
        return key;
    }
    public boolean isReward(){
        return getRewards() != null && getRewards().size() > 0;
    }
    public boolean isMoneyReward(){
        return getMoneyReward() > 0;
    }

    public String getAchievementName() {
        return achievementName;
    }
}
