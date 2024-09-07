package pl.supereasy.sectors.core.chat;

import com.google.common.cache.Cache;
import org.bukkit.entity.Player;
import org.redisson.api.RMap;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.user.impl.User;

import java.util.HashMap;
import java.util.UUID;

public interface ChatManager {

    void execute(ChatActionExecutor executor);

    RMap<String, Boolean> getChatOptions();

    SectorPlugin getPlugin();

    Cache<UUID, Long> getTimes();

    void updateStatus();

    boolean isStatus();
    boolean isPremiumStatus();

    boolean canSendMessage(User user);
}
