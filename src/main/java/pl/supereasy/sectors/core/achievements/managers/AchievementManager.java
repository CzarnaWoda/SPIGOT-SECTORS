package pl.supereasy.sectors.core.achievements.managers;

import pl.supereasy.sectors.core.achievements.data.Achievement;
import pl.supereasy.sectors.core.achievements.enums.AchievementType;
import pl.supereasy.sectors.core.user.impl.User;

import java.util.HashMap;
import java.util.List;

public interface AchievementManager {

    void createAchievement(Achievement achievement);

    Achievement getAchievement(String key);

    List<Achievement> getAchievements();

    List<Achievement> getAchievements(AchievementType type);

    void complete(User user, Achievement achievement);

    boolean isComplete(User user, Achievement achievement);
}
