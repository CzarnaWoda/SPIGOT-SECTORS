package pl.supereasy.sectors.core.chat.managers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.entity.Player;
import org.redisson.api.RMap;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.chat.ChatActionExecutor;
import pl.supereasy.sectors.core.chat.ChatManager;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.Time;
import pl.supereasy.sectors.util.TimeUtil;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ChatManagerImpl implements ChatManager {

    private final RMap<String, Boolean> chatOptions;
    private final Cache<UUID, Long> times;
    private final SectorPlugin plugin;
    private boolean status;
    private boolean premiumStatus;
    public ChatManagerImpl(SectorPlugin plugin){

        this.plugin = plugin;
        this.chatOptions = plugin.getRedissonClient().getMap("chatOptions");
        chatOptions.putIfAbsent("status", true);
        chatOptions.putIfAbsent("premium",false);

        this.status = chatOptions.get("status");
        this.premiumStatus = chatOptions.get("premium");
        this.times = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.SECONDS).build();

    }

    @Override
    public void execute(ChatActionExecutor executor) {
        ChatActionExecutor.getExecute(executor, this);
    }

    @Override
    public SectorPlugin getPlugin() {
        return plugin;
    }

    @Override
    public void updateStatus() {
        this.status = chatOptions.get("status");
        this.premiumStatus = chatOptions.get("premium");
    }

    public boolean isPremiumStatus() {
        return premiumStatus;
    }

    public void setPremiumStatus(boolean premiumStatus) {
        this.premiumStatus = premiumStatus;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    @Override
    public RMap<String, Boolean> getChatOptions() {
        return chatOptions;
    }

    @Override
    public Cache<UUID, Long> getTimes() {
        return times;
    }

    @Override
    public boolean canSendMessage(User user) {
        if (user.getGroup().hasPermission(UserGroup.HELPER)) {
            return true;
        }
        Long time = times.getIfPresent(user.getUUID());
        return time == null;
    }
}
