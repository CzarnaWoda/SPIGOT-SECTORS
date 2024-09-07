package pl.supereasy.sectors.core.events.tasks;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.core.events.EventUtil;
import pl.supereasy.sectors.core.events.data.Event;
import pl.supereasy.sectors.util.ChatUtil;

public class EventTask extends BukkitRunnable {
    private final SectorPlugin plugin;
    public EventTask(SectorPlugin plugin){
        this.plugin = plugin;
    }


    @Override
    public void run() {
        plugin.getEventManager().getGlobalEvents().forEach((type, event) -> {
            if (!event.isEnable()) {
                plugin.getEventManager().removeGlobalEvent(event.getType());

                Bukkit.broadcastMessage(ChatUtil.fixColor("&d&lEVENT &8->> &7Czas eventu &d&n" + EventUtil.getNameOfEvent(event.getType()) + "&7 skonczyl sie!"));
            }
        });
    }
}
