package pl.supereasy.sectors.core.listeners;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.impl.chat.BroadcastChatMessage;
import pl.supereasy.sectors.config.BossConfig;
import pl.supereasy.sectors.core.boss.data.BossReward;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemUtil;
import pl.supereasy.sectors.util.RandomUtil;

import java.util.Collections;

public class BossDeathListener implements Listener {

    private final SectorPlugin plugin;
    public BossDeathListener(SectorPlugin plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void bossDeath(EntityDeathEvent e){
        if(e.getEntityType().equals(EntityType.PIG_ZOMBIE)){
            if(e.getEntity().getCustomName().equalsIgnoreCase(ChatUtil.fixColor(BossConfig.BOSS_NAME))){
                if(e.getEntity().getKiller() != null) {
                    //final Location loc = e.getEntity().getLocation();

                    final int rewardint = RandomUtil.getRandInt(0, plugin.getBossManager().getRewards().size() - 1);

                    final BossReward reward = plugin.getBossManager().getRewards().get(rewardint);

                    ItemUtil.giveItems(Collections.singletonList(reward.getItemStack()), e.getEntity().getKiller());

                    plugin.getBossManager().getBosses().remove(e.getEntity().getUniqueId());

                    final BroadcastChatMessage packet = new BroadcastChatMessage(ChatUtil.fixColor("&d&lBOSSY &8->> &7Gracz &6&n" + e.getEntity().getKiller().getDisplayName() + " &7zabil &a&lPOTEZNEGO BOSSA&7!"));
                    plugin.getSectorClient().sendGlobalPacket(packet);
                }
            }
        }
    }
}
